import Header from "../components/Header.js";

export default class Home {
  constructor({ $target }) {
    this.$target = $target;
  }
  render() {
    const $home = document.createElement("div");
    new Header({ $target: this.$target }).render();
  }
}
