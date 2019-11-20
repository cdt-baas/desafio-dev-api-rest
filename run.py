from endpoints.routes import app

if __name__ == '__main__':
    from config import database
    database.init_db()
    app.run(debug=True, threaded=False)
