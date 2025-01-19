import Home from "./pages/Home.js";
export default class App {
  constructor({ $target }) {
    this.$target = $target;
  }
  render() {
    const $main = document.createElement("main");
    $main.setAttribute("id", "page_content");
    new Home({ $target: this.$target }).render();
    this.$target.appendChild($main);
  }
}
