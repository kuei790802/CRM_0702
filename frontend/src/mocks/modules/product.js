import { rest } from "msw";
import product1 from "../../assets/product1.png";
import product2 from "../../assets/product2.png";
import product3 from "../../assets/product3.png";

export const productHandlers = [
  rest.get("/api/products", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json([
        {
          category: "贈禮盒裝水果冰棒",
          products: [
            {
              imageSrc: product1,
              title: "小小沁甜禮 mini版水果冰棒多口味24入分享盒",
              price: "NT$840",
            },
            {
              imageSrc: product2,
              title: "大目釋迦冰棒8入盒裝｜春選果香精裝禮盒",
              price: "NT$640",
            },
            {
              imageSrc: product3,
              title: "香瓜冰棒8入盒裝｜春選果香精裝禮盒",
              price: "NT$640",
            },
          ],
        },
        {
          category: "單一口味冰棒多人組",
          products: [
            {
              imageSrc: product2,
              title: "大目釋迦冰棒 20 入家庭號",
              price: "NT$1,200",
            },
          ],
        },
      ])
    );
  }),
];
