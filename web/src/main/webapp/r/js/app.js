/** from http://rail-chq-subver.railinc.com/svn/uilabs/trunk/approved_ui/Railinc_UI_v3/src/js/app.js **/
$(document).ready(function(){
	// mega menu
	rail.base.megaHoverOver();
	rail.base.megaHoverOut();
	//Set custom configurations for mega menu
	rail.base.megaConfig();	
	//Init menu
	//rail.base.applicationNavMenu();
	//check buttons for btn class, add it if it's not there
	rail.base.checkButtons();
});

String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g, '');
};

$(function() {
	$(".btn-ok").addClass("btn-primary");
	$(".btn-cancel").addClass("btn-danger");
	$(".btn-search").addClass("btn-inverse");
	$(".btn-add-new").addClass("btn-success");
	$("#rail-footer").hover(function() {
		$(this).find('#footer-nav').stop(true, true).slideToggle();
	}, function() {
		$(this).find('#footer-nav').stop(true, true).slideToggle();
	});
});


function highlightSearchResult(term) {
	if (!term || term === null || term.length == 0) {
		return;
	}
	$(".highlight-search-result").each(
			function(idx, element) {
				var el = $(this);
				var t = el.text();
				var pattern = new RegExp("(" + term + ")", "ig");
				t = t.replace(pattern,
						"<span class='label label-info'>$1</span>");
				el.html(t);
			});
};
