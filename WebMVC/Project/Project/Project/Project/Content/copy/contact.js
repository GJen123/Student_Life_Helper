$(document).ready(function () {
         $("#Name").focusin(function() {
			if(document.getElementById("Name").value == "Name"){
 				document.getElementById("Name").value = "";
			}
		})
		$("#Name").focusout(function() {
			if(document.getElementById("Name").value == ""){
 				document.getElementById("Name").value = "Name";
			}
		})
		
		$("#E-mail").focusin(function() {
			if(document.getElementById("E-mail").value == "E-mail"){
 				document.getElementById("E-mail").value = "";
			}
		})
		$("#E-mail").focusout(function() {
			if(document.getElementById("E-mail").value == ""){
 				document.getElementById("E-mail").value = "E-mail";
			}
		})
		
		$("#Message").focusin(function() {
			if(document.getElementById("Message").value == "Message"){
 				document.getElementById("Message").value = "";
			}
		})
		$("#Message").focusout(function() {
			if(document.getElementById("Message").value == ""){
 				document.getElementById("Message").value = "Message";
			}
		})
		
		
});