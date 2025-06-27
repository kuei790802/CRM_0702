export default function TopCarousel({ images }) {
  return (
    <div className="group relative overflow-hidden h-[220px] mb-6">
      <div className="flex w-max gap-6 flex-row-reverse animate-scroll-right group-hover:[animation-play-state:paused]">
        {[...images, ...images].map((src, i) => (
          <div
            key={`top-${i}`}
            className="w-[350px] h-[220px] rounded-xl overflow-hidden shrink-0 shadow-md"
          >
            <img
              src={src}
              alt={`top-img-${i}`}
              className="w-full h-full object-cover"
            />
          </div>
        ))}
      </div>
    </div>
    
  );
}
