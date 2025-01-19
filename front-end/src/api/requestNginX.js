export const requestNginx = async () => {
  for (let i = 1; i <= 10; i++) {
    await fetch(`http://localhost/front-end2-${i}`, {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      mode: "no-cors", // no-cors, cors, *same-origin
      cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached

      headers: {
        "Content-Type": "application/json",
        "X-Forwarded-For": "127.0.0.1", // 원하는 IP 주소를 추가
      },
      redirect: "follow", // manual, *follow, error
      referrer: "no-referrer", // no-referrer, *client
    });
  }
};
