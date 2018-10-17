package com.total.gra.portlets.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.fr.totaluap.gra.apis.service.GRAnalyticsService;
import com.fr.totaluap.gra.apis.service.models.PPBLevelDTO;
import com.fr.totaluap.gra.apis.session.util.SessionUtil;
import com.fr.totaluap.gra.eservices.generated.ws.client.soap.common.PPBLevel;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

@Controller
@RequestMapping("VIEW")
public class AccountCeilingPortletController {

	private static Log _log = LogFactoryUtil.getLog(AccountCeilingPortletController.class);

	@Autowired
	GRAnalyticsService grAnalyticsService;

	@Autowired
	ModelMapper modelMapper;

	@RenderMapping
	public String handleRequest(final Model model, final RenderRequest request, final RenderResponse response) {

		_log.info("handleRequest...");

		final List<PPBLevelDTO> listRedPPBLevel = new ArrayList<>();
		final List<PPBLevelDTO> listOrangePPBLevel = new ArrayList<>();
		final List<PPBLevelDTO> listGreenPPBLevel = new ArrayList<>();
		final List<PPBLevelDTO> listAllPPBLevel = new ArrayList<>();

		final List<PPBLevel> listPPBLevels = grAnalyticsService.getAlertService()
				.listPPBLevels(SessionUtil.getToken(request), SessionUtil.getLogin(request),
						SessionUtil.getIdnum(request))
				.getPPBLevel();

		listPPBLevels.stream().forEach(ppbLevel -> {
			final PPBLevelDTO ppbLevelDTO = modelMapper.map(ppbLevel, PPBLevelDTO.class);

			listAllPPBLevel.add(ppbLevelDTO);

			if (ppbLevelDTO.getPercentageOfUse() <= 75) {
				listGreenPPBLevel.add(ppbLevelDTO);
			} else if (ppbLevelDTO.getPercentageOfUse() > 75 && ppbLevelDTO.getPercentageOfUse() < 90) {
				listOrangePPBLevel.add(ppbLevelDTO);
			} else {
				listRedPPBLevel.add(ppbLevelDTO);
			}

		});
		// sort the list
		Collections.sort(listAllPPBLevel, (o1, o2) -> (int) (o2.getPercentageOfUse() - o1.getPercentageOfUse()));

		model.addAttribute("listAllPPBLevel", listAllPPBLevel);
		model.addAttribute("listRedPPBLevel", listRedPPBLevel);
		model.addAttribute("listOrangePPBLevel", listOrangePPBLevel);
		model.addAttribute("listGreenPPBLevel", listGreenPPBLevel);

		return "gra-accountceiling-portlet/view";
	}

}
