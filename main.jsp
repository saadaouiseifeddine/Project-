<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui"%>
<%@ taglib prefix="theme" uri="http://liferay.com/tld/theme"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="gra" uri="http://fleetonline.total.com/tld/liferay"%> 
<fmt:bundle basename="i18n.messages"></fmt:bundle>

<theme:defineObjects />
<portlet:defineObjects />

<portlet:actionURL var="searchActionURL">
	<portlet:param name="action" value="searchAction" />
</portlet:actionURL>

<div class="content" id="view-knockout">
	<div id="filterScalePrice" class="nouveau-collaborateur ">
		<div class="content_header">
			<h1>
				<fmt:message key="ScalePrice.SearchPrices" />
			</h1>
			<p>				
				<fmt:message key="ScalePrice.SearchPricesDesc" />
			</p>
			<div class="clearfix"></div>
		</div>
		<div class="filterScalePrice global_formulaire">
			<fieldset>
				<form:form name="SearchForm" modelAttribute="SearchForm"
					id="SearchScalePrice" method="post" commandName="SearchForm"
					action="${searchActionURL}">
					<div class="row">
						<label for="priceDate"> <span><fmt:message
									key="ScalePrice.DatePrices" /></span><span class="asterisque">*</span>
						</label>
						<div class="block_input">
							<form:input path="priceDate"
								class="size_2 datefield" id="priceDate"
								name="priceDate" placeholder="JJ/MM/AAAA" type="text" value="${currentDate}"/>
						</div>
						<form:errors cssClass="error" path="priceDate" /> 
					</div>

					<div class="row">
						<label for="fuel"> <span><fmt:message
									key="ScalePrice.FuelsPrices" /></span> <span class="asterisque">*</span>
						</label>

						<div class="block_input">
							<form:select path="product" class="select2" id="SelectedFuel"
								name="SelectedFuel">
								<form:option value="All">
									<fmt:message key="ScalePrice.Product_All" />
								</form:option>
								<c:forEach items="${products}" var="product">
									<form:option value="${product.friendlyCode}"><fmt:message
					key="FuelEnum.${product.friendlyCode}" /></form:option>

								</c:forEach>
							</form:select>

						</div>
					</div>
					<div class="row">
						<form:label path="typeScalePrice" for="types">
							<span><fmt:message
									key="ScalePrice.TypesScalePrices" /></span>
							<span class="asterisque">*</span>
						</form:label>

						<div class="block_input">
							<form:select path="typeScalePrice" class="select2"
								id="SelectedType" name="SelectedType">
								<form:option value="TypeScalePrice_Price_National">
									<fmt:message
										key="ScalePrice.TypeScalePrice_Price_National" />
								</form:option>
								<form:option value="TypeScalePrice_Price_International">
									<fmt:message
										key="ScalePrice.TypeScalePrice_Price_International" />
								</form:option>
							</form:select>

						</div>
					</div>
					<div class="row">
						<form:label path="country">
							<span><fmt:message key="ScalePrice.CountriesPrices" /></span>
							<span class="asterisque">*</span>
						</form:label>
						<div class="block_input">
							<form:select path="country" class="select2" id="SelectedCountry"
								name="SelectedCountry">
								<c:forEach items="${countries}" var="currentCountry">
									<c:choose>
										<c:when test="${currentCountry == 'FR'}">
											<form:option value="${currentCountry}" selected="selected">
												<fmt:message key="CountryEnum.${currentCountry}" />
											</form:option>
										</c:when>
										<c:otherwise>
											<form:option value="${currentCountry}">
												<fmt:message key="CountryEnum.${currentCountry}" />
											</form:option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>

						</div>
					</div>
					<div class="row row_submit">
						<label for="">&nbsp;</label> <a href="<portlet:renderURL />"
							class="btn_reset"><span><fmt:message
									key="ScalePrice.RestartSearch" /></span></a>
						<button type="submit">
							<span><fmt:message key="ScalePrice.ListerTarifs" /></span><i
								class="puce_submit"></i>
						</button>
					</div>
				</form:form>
			</fieldset>
		</div>
	</div>


</div>
<script src="<%=themeDisplay.getPathThemeRoot()%>/js/libraries/jquery-ui.min.js" type="text/javascript"></script>
<script src="<%=themeDisplay.getPathThemeRoot()%>/js/libraries/jquery.validate.js" type="text/javascript"></script>

