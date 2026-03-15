import { useState } from "react";
import axios from "axios";

function App() {
  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");

/*  const ask = async () => {
    const res = await axios.get("http://localhost:8080/chat", {
      params: {
          query: question
      }
          setAnswer(res.data);
    });*/

    const ask = async () => {
    const eventSource = new EventSource(
        "http://localhost:8080/chat/streamingAsk?query="  + encodeURIComponent(question)
    );
        setAnswer("");

        eventSource.onmessage = (event) => {
            setAnswer(prev => prev + event.data);
        };

        eventSource.onerror = () => {
            eventSource.close();
        };
  };

  return (
      <div style={{maxWidth:"700px", margin:"auto", padding:"40px"}}>
        <h1>ResumeGPT</h1>

        <textarea
            value={question}
            onChange={(e)=>setQuestion(e.target.value)}
            rows={4}
            style={{width:"100%"}}
            placeholder="Ask something about Jeff's resume..."
        />

        <br/><br/>

        <button onClick={ask}>
          Ask
        </button>

        <h3>Answer</h3>
        <p>{answer}</p>
      </div>
  );
}

export default App;