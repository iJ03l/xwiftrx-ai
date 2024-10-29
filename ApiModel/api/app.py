# api/app.py

from flask import Flask, request, jsonify
from model.model import Model
from py_near.account import Account
from dotenv import load_dotenv
import os
import asyncio


load_dotenv()  # Load environment variables from .env file

app = Flask(__name__)

ai_model = Model()

@app.route('/ai', methods=['POST'])
def ai():
    data = request.get_json()
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    
    if "instruction" in data:
        instruction = data['instruction']
        response = loop.run_until_complete(ai_model.process_instruction(instruction))
        if response['action'] == 'send_transaction':
            return jsonify({"transaction_hash": response["transaction_hash"], "logs": response["logs"]})
        elif response['action'] == 'check_transaction_status':
            status = loop.run_until_complete(ai_model.check_transaction_status(response['transaction_hash']))
            return jsonify({"status": status})
        return jsonify(response)
    
    elif "question" in data and "context" in data:
        question = data['question']
        context = data['context']
        response = ai_model.answer_question(question, context)
        return jsonify({"answer": response})

    return jsonify({"error": "Invalid request format"}), 400
 
if __name__ == '__main__':
    app.run(debug=True)