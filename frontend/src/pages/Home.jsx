import Banner from "../components/Home/Banner";
import Section from "../components/Home/Section";
import Highlight from "../components/Home/Highlight";
import ProductList from "../components/Home/ProductList";
import TripleCardSection from '../components/YuYu/TripleCardSection';
import TopCarousel from "../components/YuYu/TopCarousel";
import BottomCarousel from "../components/YuYu/BottomCarousel";


import Img1 from '../assets/popsicle/010626.jpg';
import Img2 from '../assets/popsicle/020626.jpg';
import Img3 from '../assets/popsicle/030626.jpg';
import Img4 from '../assets/popsicle/040626.jpg';
import Img5 from '../assets/popsicle/050626.jpg';
import Img6 from '../assets/popsicle/060626.jpg';
import Img7 from '../assets/popsicle/070626.jpg';
import Img8 from '../assets/popsicle/080626.jpg';
import Img9 from '../assets/popsicle/090626.jpg';
import Img10 from '../assets/popsicle/100626.jpg';

const lightPackProducts = [
  {
    id: "062601",
    imageSrc: Img1,
    title: "良野頂級熟韻(巧克力)豆乳雪糕",
    price: "NT$300000",
    tag: "NEW"
  },
  {
    id: "062602",
    imageSrc: Img2,
    title: "良野特級日式(抹茶)豆乳雪糕",
    price: "NT$910",
    tag: "含果粒全新概念冰棒"
  },
  {
    id: "062603",
    imageSrc: Img3,
    title: "良野莓好生活(草莓)豆乳雪糕",
    price: "NT$910",

  },
  {
    id: "062604",
    imageSrc: Img4,
    title: "良野濃醇(花生)豆乳雪糕",
    price: "NT$910",

  },
  {
    id: "062605",
    imageSrc: Img5,
    title: "良野超越經典(香草)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062606",
    imageSrc: Img6,
    title: "良野大花之吻(玫瑰)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062607",
    imageSrc: Img7,
    title: "良野法式(焦糖佐脆餅)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062608",
    imageSrc: Img8,
    title: "良野濃(黑芝麻)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062609",
    imageSrc: Img9,
    title: "良野藍色狂想曲(藍莓)豆乳雪糕",
    price: "NT$",

  },
  {
    id: "062610",
    imageSrc: Img10,
    title: "良野轉轉(OREO)豆乳雪糕",
    price: "NT$",

  }
];

function Home() {

  const imagePaths = [
    "/images/062701.png",
    "/images/062702.png",
    "/images/062703.png",
    "/images/062704.png",
    "/images/062705.png",
    "/images/062706.png",
    "/images/062707.png",
    "/images/062708.png",
    "/images/062709.png",
    "/images/062710.png",
  ];

  return (
    <>
      <Banner />
      <Highlight />
      <Section />
      <TripleCardSection />
      <ProductList products={lightPackProducts} />
      <TopCarousel images={imagePaths.slice(0, 5)} />
      <BottomCarousel images={imagePaths.slice(5)} />
    </>
  );
}
export default Home;