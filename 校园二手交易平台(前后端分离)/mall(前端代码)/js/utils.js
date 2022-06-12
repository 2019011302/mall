function getUrlParam(key) {
	var url = window.location.toString();
	var arr = url.split("?"); //得到一个数组
	if (arr.length > 1) {
		var params = arr[1].split("&")
		for (var i = 0; i < params.length; i++) {
			var param = params[i].split("="); //得到数组
			if (param[0] == key) {
				console.log(param[0])
				return param[1];
			}
		}
	}
	return null;
}
//判断整数
function isNumber(number) {
	if (number != null) {
		for (var i = 0; i < number.length; i++) {
			if (!(number[i] >= '0' && number[i] <= '9')) {
				return false
			} else {
				return true
			}
		}
	}
}
function padDate(value){
				return value<10?'0'+value:value;
			}
function fromatDate() {
	var date1 = new Date();
	var year = date1.getFullYear();
	var month = padDate(date1.getMonth() + 1);
	var day = padDate(date1.getDate());
	var h = padDate(date1.getHours());
	var m = padDate(date1.getMinutes());
	var s = padDate(date1.getSeconds());
	return year + '-' + month + '-' + day + " " + h + ":" + m + ":" + s;
}

function showDate(date) {
	var date1 = new Date(date);
	var year = date1.getFullYear();
	var month = padDate(date1.getMonth() + 1);
	var day = padDate(date1.getDate());
	var h = padDate(date1.getHours());
	var m = padDate(date1.getMinutes());
	var s = padDate(date1.getSeconds());
	return year + '-' + month + '-' + day + "\n" + h + ":" + m + ":" + s;
}



