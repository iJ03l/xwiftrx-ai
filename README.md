<p align="center">
  <a href="https://github.com/iJ03l/xwiftrx-ai"><img src="./images/banner.png" alt="project banner"></a>
</p>

## XWIFTRX AI for [REDACTED] Hackathon

Introducing Xwiftrx AI, your all-in-one solution for navigating the Web3 landscape with confidence. Our AI safeguards you from scams, streamlines NEAR transactions, and simplifies token bridging—all within a convenient overlay. With Xwiftrx AI, you can effortlessly manage your Web3 activities without leaving your current app, ensuring a secure and efficient experience. Embrace the future of blockchain interactions with Xwiftrx AI! 🌟

# submitted for the native track category

## Technologies Used~

- Near Protool
- Py-Near
- Huggingface
- Kotlin
- Ollama
- Google colab

## How we Built it

- The app was built with kotlin as we needed native feature
- collated datas and created a dataset that was deployed on [HuggingFace-repo](https://huggingface.co/datasets/yorxsm/neart-dataset)[2](https://huggingface.co/datasets/yorxsm/neartrx-dataset)
- Picked a LLM with prompt function capabilities and trained it with the dataset on colab and deployed it [here](https://huggingface.co/yorxsm/xw_model)
- Used ollama to run the model, _lack of compute power_
- wrote a python script to interface the trained model with the frontend, using py-near to handle transactions
- Deployed on a cloud platform to test app functionalities with the ai. _too much cost- was taken down after test_

## Recreate function with CLI

- Download the [model](https://huggingface.co/yorxsm/xw_model), create a modelfile, serve with ollama, add testnet details to env, launch python app.py and python test_cli.py
- works perfect prompt like: What is near?, send 2 near to a testnet address, is this link safe.

## Challenges faced

## License

This work is licensed under a <a rel="license" href="https://mit-license.org/">MIT License</a>.
