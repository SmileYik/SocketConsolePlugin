var request = {
    postJSON: function (url, params, callback) {
        var ajaxObj = new XMLHttpRequest();
        ajaxObj.open('post', url);
        ajaxObj.setRequestHeader("Content-type", "application/json");
        ajaxObj.onreadystatechange = function () {
            if (ajaxObj.readyState == 4 && ajaxObj.status == 200) {
                callback(ajaxObj.responseText);
            }
        }
        ajaxObj.send(JSON.stringify(params));
    },
    get: function (url, callback) {
        var ajaxObj = new XMLHttpRequest();
        ajaxObj.open('get', url);
        ajaxObj.onreadystatechange = function () {
            if (ajaxObj.readyState == 4 && ajaxObj.status == 200) {
                callback(ajaxObj.responseText);
            }
        }
        ajaxObj.send();
    }
}