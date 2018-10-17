package com.total.gra.portlets.controller;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.fr.totaluap.gra.apis.service.models.RegionalPriceEntityDTO;
import com.fr.totaluap.gra.apis.session.util.SessionUtil;
import com.fr.totaluap.gra.eservices.generated.ws.client.soap.common.ArrayOfstring;
import com.fr.totaluap.gra.eservices.generated.ws.client.soap.common.DriverCode;
import com.fr.totaluap.gra.eservices.generated.ws.client.soap.common.EnumerationItemOfstring;
import com.fr.totaluap.gra.eservices.generated.ws.client.soap.common.RefItem;
import com.fr.totaluap.gra.eservices.generated.ws.client.soap.common.RegionalPriceEntity;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.total.gra.common.controller.GRABaseController;
import com.total.gra.component.util.GRADataFormatterUtil;
import com.total.gra.portlets.model.SearchForm;
import com.total.gra.portlets.util.PriceScalePortletUtil;

@Controller
@RequestMapping("VIEW")
public class PriceScalePortletController extends GRABaseController {

	private static Log _log = LogFactoryUtil.getLog(PriceScalePortletController.class);
	private static final String DOWNLOAD_EXCEL_FILE_FOR_SCALEPRICE = "downloadExcelFileForScalePrice";
	private static final String SELECTED_PRODUCT = "selectedProduct";
	public final SimpleDateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
	public final SimpleDateFormat isodateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Autowired
	ModelMapper modelMapper;

	@RenderMapping
	public String handleRequest(final Model model, final RenderRequest request, final RenderResponse response) {

		final List<String> countries = grAnalyticsService.getPriceService().listCountries(SessionUtil.getToken(request),
				SessionUtil.getLogin(request), SessionUtil.getIdnum(request)).getString();

		final List<RefItem> products = grAnalyticsService.getPriceService().listProducts(true, false,
				SessionUtil.getToken(request), SessionUtil.getLogin(request), SessionUtil.getIdnum(request))
				.getRefItem();
		 Date date =  Calendar.getInstance().getTime();
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String today = formatter.format(date);
			
		 model.addAttribute("currentDate", today);
		model.addAttribute("countries", countries);
		model.addAttribute("products", products);

		return "gra-pricescale-portlet/view";
	}

	@RenderMapping(params = "action=viewResult")
	public String viewResult(final Model model, final RenderRequest request, final RenderResponse response) {

		model.addAttribute("priceScaleType", request.getParameter("selectedPriceScaleType"));
		model.addAttribute("priceDate", request.getParameter("selectedPriceDate"));

		model.addAttribute("country", request.getParameter("selectedCountry"));
		model.addAttribute(SELECTED_PRODUCT, request.getParameter(SELECTED_PRODUCT));

		return "gra-pricescale-portlet/resultsearch";

	}

	@ActionMapping(params = "action=searchAction")
	public void searchAction(final Model model, final ActionRequest request, final ActionResponse response,
			@Valid @ModelAttribute("SearchForm") final SearchForm searchForm, final BindingResult bindingResult) {

		_log.info("searchAction...");

		try {
			if (!bindingResult.hasErrors()) {

				final String selectedPriceScaleType = searchForm.getTypeScalePrice();
				final String selectedProduct = searchForm.getProduct();
				final String selectedCountry = searchForm.getCountry();
				final String selectedPriceDate = searchForm.getPriceDate();

				final ArrayOfstring country = new ArrayOfstring();
				country.getString().add(selectedCountry);

				final EnumerationItemOfstring products = new EnumerationItemOfstring();
				products.setFriendlyCode("All".equals(selectedProduct) ? null : selectedProduct);
				List<RefItem> list=
						grAnalyticsService.getPriceService().listProducts(
							true, false, SessionUtil.getToken(request),
							SessionUtil.getLogin(request), SessionUtil.getIdnum(request)).getRefItem();
			
				for (RefItem produit : list) {
					if (produit.getFriendlyCode().equalsIgnoreCase(products.getFriendlyCode())) {
						products.setName(produit.getName());
						products.setId(produit.getId());
						
						break;
					}
				}
				System.out.println("**********************"+products.getFriendlyCode()+"************"+products.getId()+"***********************"+products.getName());
				final String parsedDate = isodateformat.format(ddmmyyyy.parse(selectedPriceDate));

				final List<RegionalPriceEntity> prices = grAnalyticsService.getPriceService()
						.readPrices(country, products, parsedDate, SessionUtil.getToken(request),
								SessionUtil.getLogin(request), SessionUtil.getIdnum(request))
						.getRegionalPriceEntity();

				final Type listType = new TypeToken<List<RegionalPriceEntityDTO>>() {
				}.getType();
				final List<RegionalPriceEntityDTO> pricesDTO = modelMapper.map(prices, listType);

				model.addAttribute("priceslist", pricesDTO);
				request.getPortletSession().setAttribute("downloadPrices", prices);
				request.getPortletSession().setAttribute("searchForm", searchForm);
				response.setRenderParameter("selectedPriceScaleType", selectedPriceScaleType);
				response.setRenderParameter("selectedCountry", searchForm.getCountry());
				response.setRenderParameter(SELECTED_PRODUCT, selectedProduct);
				response.setRenderParameter("selectedPriceDate", selectedPriceDate);
				response.setRenderParameter("action", "viewResult");

			}

		} catch (final Exception e) {
			_log.error("Error Action Mapping: " + e.getMessage(), e);
		}
	}

	@ResourceMapping("filterResult")
	public void filterResult(final ResourceRequest request, @ModelAttribute("SearchForm") final SearchForm searchForm,
			final ResourceResponse response) {

		_log.info("filterResult...");

		try {
			_log.info("filterResult END ...");
		} catch (final Exception e) {

			_log.error(e.getMessage(), e);
		}

	}

	@ModelAttribute("SearchForm")
	public SearchForm getInitializeSearchForm() {

		return new SearchForm();
	}

	
	@ResourceMapping(DOWNLOAD_EXCEL_FILE_FOR_SCALEPRICE)
	public void downloadExcelFileForScalePrice(final Model model, final ResourceRequest request,
			final ResourceResponse response) {
		try {
			final SearchForm form = (SearchForm) request.getPortletSession().getAttribute("searchForm");
			_log.debug("pricedate from form->" + form.toString());
			String selectedProductForPrice = "";
			String productName = "";
			String date = form.getPriceDate();
			final String selectedCountry = form.getCountry();
			final String selectedDate = PriceScalePortletUtil.changeDateFormatToYYYYMMDD(form.getPriceDate());
			final String fileName = messageUtil.getLocaleMessage("ScalePrice.filename_price_excel");
			Map<String, Object> priceScale = null;
			final List<Map<String, Object>> priceScales = new ArrayList<>();
			final List<RefItem> products = grAnalyticsService.getPriceService().listProducts(false, false,
					SessionUtil.getToken(request), SessionUtil.getLogin(request), SessionUtil.getIdnum(request))
					.getRefItem();
			final Optional<RefItem> refItem = products.stream()
					.filter(product -> product.getFriendlyCode().equals(form.getProduct())).findFirst();
			if (refItem.isPresent()) {
				selectedProductForPrice = refItem.get().getId();
				productName = refItem.get().getName();
			}
			_log.debug("selectedProductForPrice========>" + selectedProductForPrice);
			final ArrayOfstring country = new ArrayOfstring();
			country.getString().add(selectedCountry);

			EnumerationItemOfstring product = null;
			if (!"All".equals(selectedProductForPrice)) {
				product = new EnumerationItemOfstring();
				product.setFriendlyCode(form.getProduct());
				product.setId(selectedProductForPrice);
				product.setName(productName);
			}
			_log.debug("country-------------->>>" + country);
			_log.debug("product-------------->>>" + product);
			_log.debug("selectedDate------------->>>" + selectedDate);
			final List<RegionalPriceEntity> priceList = grAnalyticsService.getPriceService()
					.readPrices(country, product, selectedDate, SessionUtil.getToken(request),
							SessionUtil.getLogin(request), SessionUtil.getIdnum(request))
					.getRegionalPriceEntity();
			_log.debug("priceList-------------->>>" + priceList.isEmpty());
			if (CollectionUtils.isEmpty(priceList)) {
				priceList.add(new RegionalPriceEntity());
				date = "";
			}
			for (final RegionalPriceEntity priceListFromDTO : priceList) {
				priceScale = new LinkedHashMap<>();
				priceScale.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_Country"),
						getTranslation("CountryEnum.", priceListFromDTO.getCountry()));
				priceScale.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_Region"),
						getTranslation("RegionEnum.", priceListFromDTO.getRegion()));
				priceScale.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_Station"),
						getTranslation("NetworkEnum.", priceListFromDTO.getNetwork()));
				priceScale.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_Carburant"),
						getTranslation("ProductScalePrice.", Validator.isNotNull(priceListFromDTO.getProduct())
								? priceListFromDTO.getProduct().getId() : ""));
				priceScale.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_DateTarification"), date);
				priceScale
						.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_DateValiditeStart"),
								Validator.isNotNull(priceListFromDTO.getStartValid()) ? PriceScalePortletUtil
										.changeDateFormatToDDMMYYYY(priceListFromDTO.getStartValid().replace("T", " "))
										: "");
				priceScale
						.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_DateValiditeEnd"),
								Validator.isNotNull(priceListFromDTO.getEndValid()) ? PriceScalePortletUtil
										.changeDateFormatToDDMMYYYY(priceListFromDTO.getEndValid().replace("T", " "))
										: "");
				priceScale.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_PrixTTC"),
						PriceScalePortletUtil.formatDoubleValue(priceListFromDTO.getPrice(), "PrixTTC"));
				priceScale.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_PrixHT"),
						PriceScalePortletUtil.formatDoubleValue(priceListFromDTO.getPrice(), "PrixHT"));
				priceScale.put(messageUtil.getLocaleMessage("ScalePrice.ScalePrice_PrixHTT"),
						PriceScalePortletUtil.formatDoubleValue(priceListFromDTO.getPrice(), "PrixHTT"));
				priceScales.add(priceScale);
			}
			_log.debug("priceScales size========>" + priceScales.size());
			super.downloadExcelFile(priceScales, request, response, fileName, "Prix_bareme");

		} catch (final Exception e) {
			_log.error(e);
		}
	}

	protected String getTranslation(final String key, final String suffix) {
		if (StringUtils.isNotEmpty(suffix)) {
			return messageUtil.getLocaleMessage(key + suffix);
		}
		return StringPool.BLANK;
	}

}
