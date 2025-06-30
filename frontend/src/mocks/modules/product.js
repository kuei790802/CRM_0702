import { rest } from "msw";
import product063001 from "../../assets/0630/063001.png";
import product063002 from "../../assets/0630/063002.png";
import product063003 from "../../assets/0630/063003.png";
import product063004 from "../../assets/0630/063004.png";
import product063005 from "../../assets/0630/063005.png";
import product063006 from "../../assets/0630/063006.png";
import product063007 from "../../assets/0630/063007.png";
import product063008 from "../../assets/0630/063008.png";
import product063009 from "../../assets/0630/063009.png";
import product063010 from "../../assets/0630/063010.png";

import Img1 from '../../assets/popsicle/010626.jpg';
import Img2 from '../../assets/popsicle/020626.jpg';
import Img3 from '../../assets/popsicle/030626.jpg';
import Img4 from '../../assets/popsicle/040626.jpg';
import Img5 from '../../assets/popsicle/050626.jpg';
import Img6 from '../../assets/popsicle/060626.jpg';
import Img7 from '../../assets/popsicle/070626.jpg';
import Img8 from '../../assets/popsicle/080626.jpg';
import Img9 from '../../assets/popsicle/090626.jpg';
import Img10 from '../../assets/popsicle/100626.jpg';


export const productHandlers = [
  rest.get("/api/cmsproducts", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json([
        {
          category: "輕包裝豆乳雪糕",
          products: [
            {
              imageSrc: Img1,
              title: "良野頂級熟韻(巧克力)豆乳雪糕",
              price: "NT$840",
            },
            {
              imageSrc: Img2,
              title: "良野特級日式(抹茶)豆乳雪糕",
              price: "NT$640",
            },
            {
              imageSrc: Img3,
              title: "良野莓好生活(草莓)豆乳雪糕",
              price: "NT$640",
            },
            {
              imageSrc: Img4,
              title: "良野濃醇(花生)豆乳雪糕",
              price: "NT$640",
            },
            {
              imageSrc: Img5,
              title: "良野超越經典(香草)豆乳雪糕",
              price: "NT$640",
            },
            {
              imageSrc: Img6,
              title: "良野大花之吻(玫瑰)豆乳雪糕",
              price: "NT$640",
            },
            {
              imageSrc: Img7,
              title: "良野法式(焦糖佐脆餅)豆乳雪糕",
              price: "NT$640",
            },
            {
              imageSrc: Img8,
              title: "良野濃(黑芝麻)豆乳雪糕",
              price: "NT$640",
            },
            {
              imageSrc: Img9,
              title: "良野藍色狂想曲(藍莓)豆乳雪糕",
              price: "NT$640",
            },
            {
              imageSrc: Img10,
              title: "良野轉轉(OREO)豆乳雪糕",
              price: "NT$640",
            },
          ],
        },
        {
          category: "經典杯裝口味",
          products: [
            {
              imageSrc: product063001,
              title: "超越經典香草豆乳冰淇淋",
              price: "NT$1,200",
            },
            {
              imageSrc: product063002,
              title: "藍色狂想曲(藍莓)豆乳冰淇淋",
              price: "NT$1,200",
            },
            {
              imageSrc: product063003,
              title: "莓好生活豆乳冰淇淋",
              price: "NT$1,200",
            },
            {
              imageSrc: product063004,
              title: "濃醇花生豆乳冰淇淋",
              price: "NT$1,200",
            },
            {
              imageSrc: product063005,
              title: "百香果雪酪冰淇淋",
              price: "NT$1,200",
            },
            {
              imageSrc: product063006,
              title: "大花之吻(玫瑰)豆乳冰淇淋",
              price: "NT$1,200",
            },
            {
              imageSrc: product063007,
              title: "法式焦糖佐脆餅豆乳冰淇淋",
              price: "NT$1,200",
            },
            {
              imageSrc: product063008,
              title: "特級日式抹茶豆乳冰淇淋",
              price: "NT$1,200",
            },
            {
              imageSrc: product063009,
              title: "頂級熟韻可可豆乳冰淇淋",
              price: "NT$1,200",
            },
            {
              imageSrc: product063010,
              title: "轉轉OERO豆乳冰淇淋",
              price: "NT$1,200",
            },
          ],
        },
      ])
    );
  }),
];
