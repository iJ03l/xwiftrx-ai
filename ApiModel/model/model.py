# ai_model/model.py

from langchain_ollama import OllamaLLM
from py_near.account import Account
from py_near.dapps.core import NEAR
import asyncio
import os
import re
from dotenv import load_dotenv
import yaml

with open('config.yaml', 'r') as file:
    config = yaml.safe_load(file)

role = config['role']
objective = config['objective']
tools = config['tools']

load_dotenv()  # Load environment variables from .env file

class Model:
    def __init__(self):
        self.role = role
        self.objective = objective
        self.tools = tools
        self.model = OllamaLLM(base_url='http://127.0.0.1:11434', model="xwiftrx")
        self.account = Account(os.getenv('ACCOUNT_ID'), os.getenv('PRIVATE_KEY'), os.getenv('RPC_URL'))

    async def process_instruction(self, instruction):
        result = self.model.generate([instruction])  # Pass instruction as a list
        print(result)  # Print the result for debugging
        response = result.generations[0][0].text  # Extract the generated text
        print("Parsed response: ", response)  # Debugging

        # Extract receiver and amount from instruction
        receiver_id_match = re.search(r'send\s(\d+(\.\d+)?)\sNEAR\s+to\s+([\w.-]+)', instruction, re.IGNORECASE)
        if receiver_id_match:
            amount_str, _, receiver_id = receiver_id_match.groups()
            amount = int(float(amount_str) * NEAR)  # Convert amount to integer in NEAR units
            print(f"Instruction recognized: Send {amount_str} NEAR to {receiver_id}")  # Debugging
            await self.account.startup()
            transaction = await self.account.send_money(receiver_id, amount)
            print("Transaction successful: ", transaction.transaction.hash)  # Debugging
            return {
                "action": "send_transaction",
                "transaction_hash": transaction.transaction.hash,
                "logs": transaction.logs
            }
        elif "check the status of a transaction" in instruction.lower():
            transaction_hash = instruction.split('transaction_hash')[1].strip(' .,"')
            return {
                "action": "check_transaction_status",
                "transaction_hash": transaction_hash
            }
        print("No matching instruction found.")  # Debugging
        return {"action": "none"}

    async def check_transaction_status(self, transaction_hash):
        await self.account.startup()
        # Placeholder for the actual status check function
        status = "completed"  # Dummy value
        return f"The status of the transaction with hash {transaction_hash} is: {status}."

    def answer_question(self, question, context):
        result = self.model.generate([question])  # Pass question as a list
        print(result)  # Print the result for debugging
        response = result.generations[0][0].text  # Extract the generated text
        return response
