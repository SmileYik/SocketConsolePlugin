var consoleSocket = {
    start: function () {
        let url = location.href
        let token = url.substring(url.indexOf('token='))
        if (token.indexOf('&') >= 0) {
            token = token.substring(0, token.indexOf('&'))
        }
        token = token.substring(token.indexOf('=') + 1)
        let host = location.host
        let wsurl = "ws://" + host + "/console/consoleSocket?token=" + token

        let logArea = document.getElementById("logArea");
        let command = document.getElementById("command-input");
        let autoScroll = document.getElementById("auto-scroll");

        let ws = new WebSocket(wsurl);

        command.onkeydown = function (e) {
            if (e.key === 'Enter') {
                let c = command.value;
                command.value = ''
                ws.send(c)
            }
        }

        ws.onmessage = function (e) {
            logArea.value = e.data
            if (autoScroll.checked) {
                logArea.scrollTop = logArea.scrollHeight
            }
        }
    }
}