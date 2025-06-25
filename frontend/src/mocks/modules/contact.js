import { rest } from "msw";

export const contactHandler = [
  rest.post("/api/contact", async (req, res, ctx) => {
    const { name, email, message } = await req.json();

    console.log("模擬後端收到：", { name, email, message });

    return res(
      ctx.status(200),
      ctx.json({ success: true, message: "我們已收到您的來信！" })
    );
  }),
];
