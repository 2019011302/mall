function showImg(fileId , imgId){
				var file = document.getElementById(fileId);
				var imgDiv = document.getElementById(imgId);
				var reader = new FileReader();
				var file1 = file.files[0];
				reader.readAsDataURL(file1);
				reader.onload = function(result) {
					imgDiv.innerHTML = '<img src="' + reader.result + '" alt=""/>'
				}
			}