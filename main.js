$("#listSelected")
		.dataTable(
				{

					"bPaginate" : false,
					'bLengthChange' : false,
					"bInfo" : false,
					"bSort" : false,
					"language" : {
						"emptyTable" : " "
					},
					"columnDefs" : [ {
						"visible" : false,
						"targets" : 0
					}, {
						"visible" : false,
						"targets" : 1
					}, {
						"visible" : true,
						"targets" : 2,
						"searching" : true,
					}, {
						"visible" : true,
						"targets" : 3,
						"searching" : true
					} ],

					"order" : [ [ 0, 'asc' ] ],

					"drawCallback" : function(settings) {
						var api = this.api();
						var rows = api.rows({
							page : 'current'
						}).nodes();
						var last = null;

						api
								.column(0, {
									page : 'current'
								})
								.data()
								.each(
										function(group, i) {
											if (last !== group) {
												$(rows)
														.eq(i)
														.before(
																'<tr class="row"><td colspan="10" id="group" class="align_left"><strong>'
																		+ group
																		+ '</strong></td></tr>');

												last = group;
											}
										});

					}

				});

var colNetwork = document
		.querySelectorAll('#listSelected tbody tr  td:nth-child(1)');
var usedNetwork = [];
var fragNetwork = document.createDocumentFragment();
for (var i = 0; i < colNetwork.length; ++i) {
	var textNetwork = colNetwork[i].textContent;
	if ((usedNetwork.indexOf(textNetwork) == -1)
			&& (textNetwork.includes('-') == false)) {

		usedNetwork.push(textNetwork);

		var inputs = $("#Networkcheckbox").find('input');
		var id = inputs.length + 1;

		$("#Networkcheckbox").append($('<div />', {
			'class' : 'filter_checkbox'
		}));

		$("#Networkcheckbox").append($('<input />', {
			type : 'checkbox',
			name : 'Network',
			'class' : 'checkbox-label',
			id : 'Reseau' + id,
			value : textNetwork
		}));

		$("#Networkcheckbox").append($('<label />', {
			'for' : 'Reseau' + id,
			'class' : 'css-label',
			text : textNetwork
		}));

	}
	$('#listSelected tbody tr  td:nth-child(1)').hide();
	$('#listSelected thead tr  th:nth-child(1)').hide();
	$('#listSelected tbody tr  td#group:nth-child(1)').show();

}
var colRegion = document
		.querySelectorAll('#listSelected tbody tr  td:nth-child(2)');
var usedRegion = [];
var fragRegion = document.createDocumentFragment();
for (var i = 0; i < colRegion.length; ++i) {
	var textRegion = colRegion[i].textContent;
	if (usedRegion.indexOf(textRegion) == -1) {
		usedRegion.push(textRegion);

		var inputs = $("#Regioncheckbox").find('input');
		var id = inputs.length + 1;

		$("#Regioncheckbox").append($('<div />', {
			'class' : 'filter_checkbox'
		}));

		$("#Regioncheckbox").append($('<input />', {
			type : 'checkbox',
			name : 'Region',
			'class' : 'checkbox-label',
			id : 'Region' + id,
			value : textRegion
		}));

		$("#Regioncheckbox").append($('<label />', {
			'for' : 'Region' + id,
			'class' : 'css-label',
			text : textRegion
		}));

	}

}

$('#listSelected tbody tr  td:nth-child(2)').hide();
$('#listSelected thead tr  th:nth-child(2)').hide();
$('.ui-dialog').hide();
$('#Waitblack').hide();
$('.overflow-mobile').css("visibility", "visible");

function showNetworklist() {

	$("#titreNetwork").toggleClass("titre");
	$("#titreNetwork").toggleClass("titre up");
	$("#listNetwork").toggleClass("filtre filtres_actif");
	$("#listNetwork").toggleClass("filtre filtre_compte");

	$("#titreRegion").removeClass('titre up');
	$("#titreRegion").addClass('titre');
	$("#listRegion").removeClass('filtre filtres_actif');
	$("#listRegion").addClass('filtre filtre_compte');

}

function showRegionlist() {

	$("#titreRegion").toggleClass("titre");
	$("#titreRegion").toggleClass("titre up");
	$("#listRegion").toggleClass("filtre filtres_actif");
	$("#listRegion").toggleClass("filtre filtre_compte");

	$("#titreNetwork").removeClass('titre up');
	$("#titreNetwork").addClass('titre');
	$("#listNetwork").removeClass('filtre filtres_actif');
	$("#listNetwork").addClass('filtre filtre_compte');

}
function uncheckRegionFilter() {
	$('input:checkbox[name="Region"]:checked').prop('checked', false);
	filterme();
}
function uncheckNetowrkFilter() {
	$('input:checkbox[name="Network"]:checked').prop('checked', false);
	filterme();
}
function filterme() {
	$('.ui-dialog').show();
	$('#Waitblack').show();

	var Region = $('input:checkbox[name="Region"]:checked').map(function() {
		return this.value;

	}).get().join('|');

	var network = $('input:checkbox[name="Network"]:checked').map(function() {
		return this.value;
	}).get().join('|');
	if ($('input:checkbox[name="Region"]:checked').length == 0)
		$("a").remove('.RegionSelected');

	else if ($('input:checkbox[name="Region"]:checked').length == 1) {
		$("a").remove('.RegionSelected');
		$("#filterCriteriaList")
				.append(
						'<a class="RegionSelected" onclick="$(this).remove();uncheckRegionFilter()">Région : '
								+ Region + '</a>');
		$("#filtersDIV-activeFilters")
				.append(
						'<a class="RegionSelected" onclick="$(this).remove();uncheckRegionFilter()">Région : '
								+ Region + '</a>');

	} else {
		$("a").remove('.RegionSelected');
		$("#filterCriteriaList")
				.append(
						'<a class="RegionSelected" onclick="$(this).remove();uncheckRegionFilter()">Région : '
								+ $('input:checkbox[name="Region"]:checked').length
								+ ' valeurs</a>');
		$("#filtersDIV-activeFilters")
				.append(
						'<a class="RegionSelected" onclick="$(this).remove();uncheckRegionFilter()">Région : '
								+ $('input:checkbox[name="Region"]:checked').length
								+ ' valeurs</a>');

	}

	if ($('input:checkbox[name="Network"]:checked').length == 0)
		$("a").remove('.NetworkSelected');

	else if ($('input:checkbox[name="Network"]:checked').length == 1)

	{
		$("a").remove('.NetworkSelected');
		$("#filterCriteriaList")
				.append(
						'<a class="RegionSelected" onclick="$(this).remove(),uncheckNetowrkFilter()">Réseau : '
								+ network + '</a>');
		$("#filtersDIV-activeFilters")
				.append(
						'<a class="RegionSelected" onclick="$(this).remove(),uncheckNetowrkFilter()">Réseau : '
								+ network + '</a>');
	} else {
		$("a").remove('.NetworkSelected');
		$("#filterCriteriaList")
				.append(
						'<a class="NetworkSelected" onclick="$(this).remove(),uncheckNetowrkFilter()">Réseau : '
								+ $('input:checkbox[name="Network"]:checked').length
								+ ' valeurs</a>');
		$("#filtersDIV-activeFilters")
				.append(
						'<a class="NetworkSelected" onclick="$(this).remove(),uncheckNetowrkFilter()">Réseau : '
								+ $('input:checkbox[name="Network"]:checked').length
								+ ' valeurs</a>');

	}
	$('#listSelected').dataTable().fnFilter(Region, 3, true, false, false,
			false);
	$('#listSelected').dataTable().fnFilter(network, 2, true, false, false,
			false);
	$('body').css('overflow', 'visible');
	$('.ui-dialog').hide();
	$('#Waitblack').hide();
}
function showDiv() {

	if (document.getElementById("filterForm").style.display == "none") {
		document.getElementById("filterForm").style.display = "block";
		document.getElementById('disableback').style.display = "block";
		$('body').css('overflow', 'hidden');
	} else {
		document.getElementById("filterForm").style.display = "none";
		document.getElementById('disableback').style.display = "none";
		$('body').css('overflow', 'visible');
	}
}
function closeFilter() {

	document.getElementById("filterForm").style.display = "none";
	document.getElementById('disableback').style.display = "none";
	$('body').css('overflow', 'visible');

}

$(document).ready(function() {
	$('.datefield_').datepicker('setDate', new Date());
	$('.select2').select2();
	$('.dataTables_filter').hide();
	$("#filterRegion").keyup(function() {

		var filter = $(this).val();
		$("div div label").each(function() {

			if ($(this).text().search(new RegExp(filter, "i")) < 0) {
				$(this).fadeOut();
			} else {
				$(this).show();

			}
		});

	});
	$("#filterNetwork").keyup(function() {

		var filter = $(this).val();
		$("div div label").each(function() {

			if ($(this).text().search(new RegExp(filter, "i")) < 0) {
				$(this).fadeOut();
			} else {
				$(this).show();

			}
		});

	});
	$(document).on('click', "#downloadExcel", function() {
		var json = $('table#listSelected tr td').map(function() {

			return $(this).html();
		}).get();

		$.ajax({
			url : "${downloadExcelFile}",

			data : {
				json : json
			},
			dataType : 'json',
			contentType : "application/json",
			success : function(data) {
				filterme();

			},
			error : function() {
			},
		});
	});
});
