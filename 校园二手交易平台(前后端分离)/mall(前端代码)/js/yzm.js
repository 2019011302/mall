function yzm(slector, w, h) {
			var result = "";
			//生成随机数
			function rn(min, max) {
				return parseInt(Math.random() * (max - min) + min);
			}
			//生成随机颜色
			function rc(min, max) {
				var r = rn(min, max);
				var g = rn(min, max);
				var b = rn(min, max);
				return `rgb( ${r} , ${g} , ${b} )`
			}
			//设计画布样式和背景色
			var w = w;
			var h = h;
			var canvas = document.getElementById(slector)
			var ctx = canvas.getContext('2d');
			ctx.fillStyle = rc(180, 230);
			ctx.fillRect(0, 0, w, h);

			//随机字符串
			var pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			for (var i = 0; i < 4; i++) {
				//随机获取字符
				var ch = pool[rn(0, pool.length)];
				//设置字符颜色
				ctx.fillStyle = rc(70, 150);
				//设置字体大小以及样式
				var size = rn(18, 40);
				ctx.font = size + 'px Simhei';
				//设置旋转角度
				var deg = rn(-30, 30);
				ctx.textBaseline = 'top';
				ctx.save();
				//水平位移
				ctx.translate(30 * i + 15, 15);
				ctx.rotate(deg * Math.PI / 180);
				ctx.fillText(ch, -10, -10);
				ctx.restore();
				result += ch;
			}
			//增加线条干扰
			for (var i = 0; i < 5; i++) {
				ctx.beginPath();
				ctx.moveTo(rn(0, w), rn(0, h));
				ctx.lineTo(rn(0, w), rn(0, h));
				ctx.strokeStyle = rc(180, 240);
				ctx.closePath();
				ctx.stroke();
			}
			//增加原点干扰
			for (var i = 0; i < 40; i++) {
				ctx.beginPath();
				ctx.arc(rn(0, w), rn(0, h), 1, 0, 2 * Math.PI)
				ctx.closePath();
				ctx.fillStyle = rc(150, 200);
				ctx.fill();
			}
			return result;
		}