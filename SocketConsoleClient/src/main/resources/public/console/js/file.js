const iofile = {
  ls_btn: function () {
      let path = document.getElementById("path").value;
      request.get("/console/io/ls?path=" + path, function (text) {
          document.getElementById("ls").value = path + "目录下的文件为: \n" + text
      })
  },
  cat_btn: function () {
      let path = document.getElementById("path").value + "/" + document.getElementById("edit-path").value;
      request.get("/console/io/cat?path=" + path, function (text) {
          document.getElementById("edit-area").value = text
      })
  },
  write_btn: function () {
      let path = document.getElementById("path").value + "/" + document.getElementById("edit-path").value;
      request.postJSON(
          "/console/io/write?path=" + path,
          {
              b: document.getElementById("edit-area").value
          }, function (text) {
              alert(text);
          }
      )
  }
};