from app import create_app

app = create_app()

if __name__ == "__main__":
    print("Server run")
    app.run(debug=True, port=3000)


# sphinx-build -b html source build/html
