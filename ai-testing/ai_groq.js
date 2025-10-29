const Groq = require("groq-sdk");

module.exports = async function (messages) {
    const client = new Groq({ apiKey: process.env.GROQ_API_KEY });

    const completion = await client.chat.completions.create({
        model: "llama-3.3-70b-versatile",
        messages: messages,
    });

    return completion.choices[0]?.message?.content;
};
