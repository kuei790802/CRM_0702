export default function IcePopCardGrid() {
  const pops = [
    { name: "超越經典香草", img: "/images/062901.png", link: "/Store" },
    { name: "藍色狂想曲", img: "/images/062902.png", link: "/Store" },
    { name: "莓好生活", img: "/images/062903.png", link: "/Store" },
    { name: "濃醇花生", img: "/images/062904.png", link: "/Store" },
    { name: "百香果雪酪", img: "/images/062905.png", link: "/Store" },
    { name: "大花之吻", img: "/images/062906.png", link: "/Store" },
    { name: "法式焦糖佐脆餅", img: "/images/062907.png", link: "/Store" },
    { name: "特級日式抹茶", img: "/images/062908.png", link: "/Store" },
    { name: "頂級熱韻可可", img: "/images/062909.png", link: "/Store" },
    { name: "轉轉OREO", img: "/images/062910.png", link: "/Store" },
  ];

  return (
    <div className="max-w-6xl mx-auto py-6 pb-16">
      <h2 className="text-center text-2xl md:text-3xl font-bold tracking-wider mb-10 text-gray-800">
        熱門經典杯子口味
      </h2>

      <div className="grid grid-cols-2 md:grid-cols-5 gap-6 place-items-center">
        {pops.map((pop, i) => (
          <div key={i} className="group flex flex-col items-center space-y-4">
            <a href={pop.link}>
              <img
                src={pop.img}
                alt={pop.name}
                className="h-[160px] object-contain hover:scale-105 transition-transform duration-300"
              />
            </a>
            <span
              className={`text-sm md:text-base font-semibold tracking-widest text-black group-hover:text-logo-blue`}
            >
              {pop.name}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}
