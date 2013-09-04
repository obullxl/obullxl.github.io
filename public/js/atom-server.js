function fadeProgressModal(visiable) {
	if (visiable) {
		$("#modal-progress").modal("show");
	} else {
		$("#modal-progress").modal("hide");
	}
}

function fadeSuccessModal(data) {
	$("#success-biz-log").text(data.bizLog);
	$("#modal-success").modal("show");
}

function fadeFailureModal(data) {
	$("#failure-biz-log").text(data.bizLog);
	$("#failure-resp-code").text(data.respCode);
	$("#failure-resp-desp").text(data.respDesp);
	$("#modal-failure").modal("show");
}

/*
setTimeout(function() {
	$('.sidebar-nav').affix()
}, 100);
*/
