/**  
* Dependancies: this library depends on the jQuery Library, as well as the jQuery UI Library
* Author: Adrian Pomilio
* Purpose: Railinc Base Standard JavaScript Library 
**/

var rail = this.rail || {};

rail.base = (function () {
	var i = {
		requiredLabel: function () {
			$("label.required").prepend('<span class="requiredSymbol">* </span>');
		},
		listSwapper: function () {
			$(".swap input[type='button']").click(function () {  
				var arr = $(this).attr("name").split("2");  
				var from = arr[0];  
				var to = arr[1];  
				$("#" + from + " option:selected").each(function () {  
					$("#" + to).append($(this).clone());  
					$(this).remove();  
				});  
			});
		},
		megaHoverOver: function () {
			$(this).find("#rail-launch-pad").stop().fadeTo('fast', 1).show(); //Find sub and fade it in
			(function($) {
				//Function to calculate total width of all ul's
				jQuery.fn.calcSubWidth = function () {
					rowWidth = 0;
					//Calculate row
					$(this).find("ul").each(function () { //for each ul...
						rowWidth += $(this).width(); //Add each ul's width together
					});
				};
			})(jQuery); 

			if ( $(this).find(".row").length > 0 ) { //If row exists...
			
				var biggestRow = 0;	
		
				$(this).find(".row").each(function () {	//for each row...
					$(this).calcSubWidth(); //Call function to calculate width of all ul's
					//Find biggest row
					if(rowWidth > biggestRow) {
						biggestRow = rowWidth;
					}
				});

				$(this).find("#rail-launch-pad").css({'width' :biggestRow}); //Set width
				$(this).find(".row:last").css({'margin':'0'});  //Kill last row's margin
			} else { //If row does not exist...
				$(this).calcSubWidth();  //Call function to calculate width of all ul's
				$(this).find(".sub").css({'width' : rowWidth}); //Set Width
			}
		},
		megaHoverOut: function () {
			$(this).find("#rail-launch-pad").stop().fadeTo('fast', 0, function () { //Fade to 0 opactiy
				$(this).hide();  //after fading, hide it
			});
		},
		megaConfig: function () {
			var config = {
				sensitivity: 2, // number = sensitivity threshold (must be 1 or higher)
				interval: 100, // number = milliseconds for onMouseOver polling interval
				over: i.megaHoverOver, // function = onMouseOver callback (REQUIRED)
				timeout: 500, // number = milliseconds delay before onMouseOut
				out: i.megaHoverOut // function = onMouseOut callback (REQUIRED)
			};

			$("#rail-launch-pad").css({'opacity':'0'}); //Fade sub nav to 0 opacity on default
			$("#util-nav li").hoverIntent(config); //Trigger Hover intent with custom configurations
		},
		applicationNavMenu: function () {
			$('#rail-app-nav').supersubs({ minWidth: 13, maxWidth: 27, extraWidth: 1 }).superfish({ animation: {opacity:'show',height:'show'},delay: 100, speed: 'fast'});
		},
		datePicker: function () {
			$( ".dpField" ).datepicker({
				showOn: "button",
				buttonImage: "images/calendar.gif",
				buttonImageOnly: false
			});
		},
		checkButtons: function() {
			$('button, input[type=button], input[type=submit]').each(function() {
				if (!$(this).hasClass('btn')) {
					$(this).addClass("btn");
				}
			});
		},
		accordion: function(target) {
			$squeeze = $("#"+target);
			$squeeze.find("h3").html('<a href="#">' + $squeeze.find("h3").html() + "</a>");
			
			//jQuery UI needs to be loaded for this to work!
			$squeeze.accordion({
				collapsible: true,
				clearStyle: true,
				active: 1
			});
		},
		selectableTable: function(table, selectAll, selectId, valueIdx) {
			var showSelectAll = selectAll ? selectAll : false,
				selectId = selectId ? selectId : 'checkbox',
				valueIdx = valueIdx ? valueIdx : 1;
			
			
			var $table = $("#" + table); // tablesorter
			
			$header = $("thead tr", $table).prepend($('<th class="rail-select-header"></th>'));
			
			if (showSelectAll) {
				$("th.rail-select-header").append($('<input />').attr({ name : 'checkbox-select-all' }).attr({ id : 'checkbox-select-all' }).attr({ type : 'checkbox' }));
			} 
				
				
			//add checkbox to rows in the body
			var ct = 1;
			$("tbody tr", $table).each(function() {
				var $chkVal = $(this).find('td:eq('+valueIdx+')', $(this)).html();
				
				$(this).prepend($('<td></td>').append(
					$('<input />')
						.attr({ id: selectId+'-'+ct })
						.attr({ name : selectId+'-'+ct })
						.attr({ value : $chkVal })
						.attr({ type : 'checkbox' })
				));
				ct++;
			});
			
			$("#checkbox-select-all").click(function() {
				$checkboxes = $("tbody tr td input:checkbox", $table);

				if ($(this).is(":checked")) {
					$checkboxes.each(function() {
						$(this).prop('checked', true).parents("tr").addClass("selectedRow");
					});
				} else {
					$checkboxes.each(function() {
						$(this).prop('checked', false).parents("tr").removeClass("selectedRow");
						
					});
				}
			});
		},
		tableSorterUtil: function(table) {
			var $table = $("#" + table); // tablesorter

			if ($table.length) {
				$table.tablesorter({
					widgets: ['zebra']
				});

				var $tbody = $('tbody', $table);

				$(':radio, :checkbox', $tbody).live("click", function() {
					if ($(this).is(":radio")) {
						$('tr', $tbody).each(function() {
							$(this).removeClass("selectedRow");
						});
					}
     				$(this).closest('tr').toggleClass('selectedRow');
     			});
			}
			var count = $('tbody tr', $table).length,
				rowCountDisplay = $table.parent().find(".rowCount");
		
				if (count !== 1 && rowCountDisplay) {
					rowCountDisplay.html("Showing " + count + " entries.");
				} else {
					rowCountDisplay.html("Showing " + count + " entry.");
				}
			}
		}

	return i;
})();

//Superfish
(function(a){a.fn.superfish=function(d){var c=a.fn.superfish,b=c.c,j=a(['<span class="',b.arrowClass,'"> &#187;</span>'].join("")),g=function(){var c=a(this),b=h(c);clearTimeout(b.sfTimer);c.showSuperfishUl().siblings().hideSuperfishUl()},i=function(){var b=a(this),d=h(b),e=c.op;clearTimeout(d.sfTimer);d.sfTimer=setTimeout(function(){e.retainPath=-1<a.inArray(b[0],e.$path);b.hideSuperfishUl();e.$path.length&&1>b.parents(["li.",e.hoverClass].join("")).length&&g.call(e.$path)},e.delay)},h=function(a){a= a.parents(["ul.",b.menuClass,":first"].join(""))[0];c.op=c.o[a.serial];return a};return this.each(function(){var k=this.serial=c.o.length,f=a.extend({},c.defaults,d);f.$path=a("li."+f.pathClass,this).slice(0,f.pathLevels).each(function(){a(this).addClass([f.hoverClass,b.bcClass].join(" ")).filter("li:has(ul)").removeClass(f.pathClass)});c.o[k]=c.op=f;a("li:has(ul)",this)[a.fn.hoverIntent&&!f.disableHI?"hoverIntent":"hover"](g,i).each(function(){f.autoArrows&&a(">a:first-child",this).addClass(b.anchorClass).append(j.clone())}).not("."+ b.bcClass).hideSuperfishUl();var e=a("a",this);e.each(function(a){var b=e.eq(a).parents("li");e.eq(a).focus(function(){g.call(b)}).blur(function(){i.call(b)})});f.onInit.call(this)}).each(function(){var d=[b.menuClass];c.op.dropShadows&&!(a.browser.msie&&7>a.browser.version)&&d.push(b.shadowClass);a(this).addClass(d.join(" "))})};var b=a.fn.superfish;b.o=[];b.op={};b.IE7fix=function(){var d=b.op;a.browser.msie&&6<a.browser.version&&d.dropShadows&&void 0!=d.animation.opacity&&this.toggleClass(b.c.shadowClass+ "-off")};b.c={bcClass:"sf-breadcrumb",menuClass:"sf-js-enabled",anchorClass:"sf-with-ul",arrowClass:"sf-sub-indicator",shadowClass:"sf-shadow"};b.defaults={hoverClass:"sfHover",pathClass:"overideThisToUse",pathLevels:1,delay:800,animation:{opacity:"show"},speed:"normal",autoArrows:!0,dropShadows:!0,disableHI:!1,onInit:function(){},onBeforeShow:function(){},onShow:function(){},onHide:function(){}};a.fn.extend({hideSuperfishUl:function(){var d=b.op,c=!0===d.retainPath?d.$path:"";d.retainPath=!1;c=a(["li.", d.hoverClass].join(""),this).add(this).not(c).removeClass(d.hoverClass).find(">ul").hide().css("visibility","hidden");d.onHide.call(c);return this},showSuperfishUl:function(){var a=b.op,c=this.addClass(a.hoverClass).find(">ul:hidden").css("visibility","visible");b.IE7fix.call(c);a.onBeforeShow.call(c);c.animate(a.animation,a.speed,function(){b.IE7fix.call(c);a.onShow.call(c)});return this}})})(jQuery);
//Supersubs
(function(a){a.fn.supersubs=function(h){var f=a.extend({},a.fn.supersubs.defaults,h);return this.each(function(){var c=a(this),d=a.meta?a.extend({},f,c.data()):f,h=a('<li id="menu-fontsize">&#8212;</li>').css({padding:0,position:"absolute",top:"-999em",width:"auto"}).appendTo(c).width();a("#menu-fontsize").remove();$ULs=c.find("ul");$ULs.each(function(e){var e=$ULs.eq(e),g=e.children(),c=g.children("a"),f=g.css("white-space","nowrap").css("float"),b=e.add(g).add(c).css({"float":"none",width:"auto"}).end().end()[0].clientWidth/ h,b=b+d.extraWidth;if(b>d.maxWidth)b=d.maxWidth;else if(b<d.minWidth)b=d.minWidth;b+="em";e.css("width",b);g.css({"float":f,width:"100%","white-space":"normal"}).each(function(){var c=a(">ul",this),d=void 0!==c.css("left")?"left":"right";c.css(d,b)})})})};a.fn.supersubs.defaults={minWidth:9,maxWidth:25,extraWidth:0}})(jQuery);
