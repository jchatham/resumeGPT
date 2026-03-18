import { useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

function App() {
    const [question, setQuestion] = useState("");
    const [answer, setAnswer] = useState("");
    const [loading, setLoading] = useState(false);

    const ask = async () => {
        if (!question.trim()) return;

        setAnswer("");
        setLoading(true);

        try {
            const response = await fetch(
                "/chat?query=" + encodeURIComponent(question)
            );

            if (!response.ok) {
                throw new Error("API error: " + response.status);
            }

            const text = await response.text(); // get full response
            setAnswer(text);
        } catch (err) {
            console.error(err);
            setAnswer("Error fetching answer.");
        } finally {
            setLoading(false);
        }
    };
    const cleanAnswer = answer
        .split("\n")
        .map(line => line.trimStart()) // remove leading spaces
        .join("\n");
    return (
        <div style={{ maxWidth: 700, margin: "auto", padding: 40 }}>
            <h1>ResumeGPT</h1>
            <h2>Jefferey Chatham's Resume AI</h2>
            <h3>Common prompts include asking about "Contact info", "Experience", "Education", "Projects", "Summarize resume", "Tell me about ResumeGPT", or "Give me an elevator pitch"</h3>
            <h4>AI hallucinates - check primary sources</h4>
            <h4><a href="mailto:jeffereychatham@protonmail.com">email</a></h4>
            <textarea
                rows={3}
                value={question}
                onChange={(e) => setQuestion(e.target.value)}
                onKeyDown={(e) => {
                    if (e.ctrlKey && e.key === "Enter") {
                        ask();
                    }
                }}
                style={{ width: "100%" }}
                placeholder="Ask about Jeff's resume. Responses can take a while...."
            />

            <br />
            <br />

            <button onClick={ask} disabled={loading}>
                {loading ? "Loading..." : "Ask"}
            </button>

            <hr />

            <div>
                <ReactMarkdown remarkPlugins={[remarkGfm]}>
                    {cleanAnswer}
                </ReactMarkdown>
            </div>
        </div>
    );
}

export default App;