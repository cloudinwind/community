$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
	// 判断当前的状态, 前端 th:class="|btn ${hasFollowed?'btn-secondary':'btn-info'} btn-sm float-right mr-5 follow-btn|“ 已经设置
	// 如果有 btn-info 属性, 则当前状态是 当前登录用户未关注该用户, 点击该按钮后是 关注的动作
	if($(btn).hasClass("btn-info")) {
		// 关注TA
		$.post(
			CONTEXT_PATH + "/follow",
			// entityType: 3 表示关注的是User
			{"entityType":3,"entityId":$(btn).prev().val()},
			function(data) {
				data = $.parseJSON(data);
				// console.log(data);
				if(data.code == 0) {
					// 页面刷新, 相当于会重启发起 http://localhost:8080/community/user/profile/userId 请求
					window.location.reload();
				} else {
					alert(data.msg);
				}
			}
		);
		// $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
	} else {
		// 取消关注
		$.post(
			CONTEXT_PATH + "/unfollow",
			{"entityType":3,"entityId":$(btn).prev().val()},
			function(data) {
				data = $.parseJSON(data);
				if(data.code == 0) {
					// 页面刷新, 相当于会重启发起 http://localhost:8080/community/user/profile/userId 请求
					window.location.reload();
				} else {
					alert(data.msg);
				}
			}
		);
		//$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
	}
}