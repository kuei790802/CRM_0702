import Banner from "../components/Home/Banner";
import Section from "../components/Home/Section";
import ProductList from "../components/Home/ProductList";

const lightPackProducts = [
  {
    id: "lime6",
    imageSrc: "../../assets/product1.jpg",
    title: "2025全新口味｜檸檬煉乳冰棒6入組",
    price: "NT$300",
    tag: "NEW"
  },
  {
    id: "sugarapple12",
    imageSrc: "../../assets/product1.jpg",
    title: "大目釋迦冰棒12入組｜台東大目釋迦",
    price: "NT$910",
    tag: "含果粒全新概念冰棒"
  },
  {
    id: "melon12",
    imageSrc: "../../assets/product1.jpg",
    title: "香瓜冰棒12入組｜台灣美濃瓜",
    price: "NT$910",
    
  },
  {
    id: "mango12",
    imageSrc: "../../assets/product1.jpg",
    title: "夏雪芒果冰棒12入組｜台東夏雪芒果",
    price: "NT$910",
    
  }
];

function Home() {
  return (
    <>
      <Banner />
      <Section />
      <ProductList products={lightPackProducts} />
    </>
  );
}
export default Home;