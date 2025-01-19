import { requestNginx } from "../api/requestNginX.js";

export default class Header {
  constructor({ $target }) {
    this.$target = $target;
  }
  render() {
    const $header = document.createElement("header");

    const $button = document.createElement("button");
    $button.setAttribute("id", "createBtn");
    $button.innerText = "요청하기";

    $button.addEventListener("click", async function () {
      for (let i = 0; i < 10; i++) {
        await requestNginx();
      }
    });

    $header.appendChild($button);

    this.$target.appendChild($header);
  }
}
