<%@include file="/WEB-INF/jsp/d.jsp" %>
<fieldset>
    <legend>Members</legend>
			
			
			<div class="control-group">
				<label class="control-label"><fmt:message key="usergroup.form.label.adduserid"/></label>
				<div class="controls">
					<input id="theSsoUserIdField" type="text" placeholder="SSO User Id"/>
					<span class="help-inline">Press 'space' to add and stay in the field.</span>
				</div>
			</div>
			
			<c:set var="cssClass"><spring:bind path="usergroup.userId"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="userId" cssClass="control-label"><fmt:message key="usergroup.form.label.userids"/></form:label>
				<div class="controls">
					<div id="userIds"></div>
					
					<script>
					$("#theSsoUserIdField").on("change", function(e){ 
						renderUserId($(this).val());
						$(this).val("");
					});
					$("#theSsoUserIdField").on("keyup", function(e){ 
						$(this).val($(this).val().trim());
					});
					$("#theSsoUserIdField").on("keypress", function(e){ 
						if (e.which == 32) { 
							renderUserId($(this).val().trim()); 
							$(this).val("");
						}
					});
					function removeUserId(userId) {
						$("#" + userId.trim()).remove();	
					}
					
					function renderUserId(userId) {
						userId = userId.trim();
						if ($("#" + userId).length > 0) { return; }
						if (userId.length == 0) { return; }
						var user = {
							    userId: userId
						};
						var template = "<span id='{{userId }}' style='padding-right:10px;'><input type='hidden' value='{{userId }}' name='userId'/><a class='btn btn-danger btn-remove-user' title='Remove {{userId}}'><i class='icon-user icon-white'></i> {{userId }}</a></span>";
						var html = Mustache.to_html(template, user);
						var span = $(html);
						$('#userIds').append(span);
						span.find(".btn-remove-user").tooltip();
					}

					$(function(){
						<c:forEach items="${ usergroup.userId }" var="userId">
						renderUserId('${userId}');
						</c:forEach>
						$(document).on('click', '.btn-remove-user', function(){
							removeUserId($(this).text());
						});
					});
					</script>

				</div>
			</div>
</fieldset>
			