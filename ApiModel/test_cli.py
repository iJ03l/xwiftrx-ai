# test_cli.py

import requests
import json

def send_instruction(instruction):
    url = "http://127.0.0.1:5000/ai"
    headers = {"Content-Type": "application/json"}
    data = {"instruction": instruction}
    response = requests.post(url, headers=headers, data=json.dumps(data))
    print(response.json())

def send_question(question, context):
    url = "http://127.0.0.1:5000/ai"
    headers = {"Content-Type": "application/json"}
    data = {"question": question, "context": context}
    response = requests.post(url, headers=headers, data=json.dumps(data))
    print(response.json())

if __name__ == "__main__":
    while True:
        print("Choose an option:\n1. Send instruction\n2. Send question\n3. Exit")
        choice = input("Enter choice: ")
        if choice == "1":
            instruction = input("Enter instruction: ")
            send_instruction(instruction)
        elif choice == "2":
            question = input("Enter question: ")
            context = input("Enter context: ")
            send_question(question, context)
        elif choice == "3":
            break
        else:
            print("Invalid choice. Please try again.")
