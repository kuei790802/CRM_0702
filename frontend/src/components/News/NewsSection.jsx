import { Link } from "react-router-dom";

function NewsSection({ imageSrc, date, title, id }) {
  return (
    <Link to={`/news/${id}`}>
      <div className="flex flex-col md:flex-row items-center bg-white rounded-xl shadow-md overflow-hidden my-4 w-full max-w-4xl mx-auto transition hover:scale-[1.01] hover:shadow-lg">
        <div className="w-full md:w-[300px] h-[200px] flex-shrink-0">
          <img src={imageSrc} alt={title} className="w-full h-full object-cover" />
        </div>
        <div className="p-6 flex flex-col justify-center text-left w-full">
          <p className="text-sm text-gray-500">{date}</p>
          <h2 className="text-xl font-bold mt-1 leading-snug">{title}</h2>
        </div>
      </div>
    </Link>
  );
}

export default NewsSection;
