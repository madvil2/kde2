from flask import Flask, request, send_from_directory
from src.doc_filler import fill_monitoring_card, fill_shelter_animals, fill_aggregated_shelters


_RESPONSE_DIR = 'output/'
_RESPONSE_FILE = 'response.docx'
app = Flask(__name__)


@app.route('/build', methods=['GET'])
def uploaded_file():
    doc_name = request.args.get('document')
    if doc_name is None:
        return 'You have to fill ?document='

    if doc_name == 'pet_card':
        pet_id = request.args.get('pet_id')
        if not pet_id:
            return f"You have to fill &pet_id= "
        fill_monitoring_card(pet_id, _RESPONSE_DIR + _RESPONSE_FILE)
    elif doc_name == 'shelter_animals':
        shelter_id = request.args.get('shelter_id')
        if not shelter_id:
            return f"You have to fill &shelter_id= "
        fill_shelter_animals(shelter_id, _RESPONSE_DIR + _RESPONSE_FILE)
    elif doc_name == 'aggregated_shelters':
        date_from = request.args.get('date_from')
        date_to = request.args.get('date_to')
        prefect_id = request.args.get('prefect_id')
        if not date_from or not date_to or not prefect_id:
            return f"You have to fill &date_from= &date_to= &prefect_id"
        fill_aggregated_shelters(prefect_id, date_from, date_to, _RESPONSE_DIR + _RESPONSE_FILE)
    else:
        return f"Unknown document {doc_name}"
    return send_from_directory(_RESPONSE_DIR, _RESPONSE_FILE)


if __name__ == '__main__':
    app.run(port=5000)
