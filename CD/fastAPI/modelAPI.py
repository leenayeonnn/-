import uvicorn
from fastapi import FastAPI
from pydantic import BaseModel
import joblib
from konlpy.tag import Twitter
#from sklearn.feature_extraction.text import TfidfVectorizer


import pickle

app = FastAPI()

model_path_pos_neg = 'fastAPI\\model\\modelMyData.pkl'

with open(model_path_pos_neg, 'rb') as model_file:
    model_for_pos_neg = pickle.load(model_file)

model_path_sad_mad = 'fastAPI\\model\\modelMyData_negative.pkl'

with open(model_path_sad_mad, 'rb') as model_file:
    model_for_sad_mad = pickle.load(model_file)

twitter = Twitter()
def tw_tokenizer(text):
    # 입력 인자로 들어온 text 를 형태소 단어로 토큰화 하여 list 객체 반환
    tokens_ko = twitter.morphs(text)
    return tokens_ko

tfidf_path_pos_neg = 'fastAPI\\model\\tfidf_vect.pkl'
tfidf_path_sad_mad = 'fastAPI\\model\\tfidf_vect_negative.pkl'
tfidf_for_pos_neg = joblib.load(tfidf_path_pos_neg)
tfidf_for_sad_mad = joblib.load(tfidf_path_sad_mad)
    
class TextRequest(BaseModel):
    text: str

@app.post("/predict/positiveOrNegative")
async def predict_positive_or_negative(text_request: TextRequest):
    tfidf_matrix = tfidf_for_pos_neg.transform([text_request.text])

    # 감정 예측
    prediction = model_for_pos_neg.predict(tfidf_matrix)

    if prediction == 1:
        result = 'Positive'
    else:
        result = 'Negative'

    return result


@app.post(path="/predict/sadOrMad")
def predictSadOrMad(text_request: TextRequest):
    tfidf_matrix = tfidf_for_sad_mad.transform([text_request.text])

    # 감정 예측
    prediction = model_for_sad_mad.predict(tfidf_matrix)

    if prediction == 1:
        result = 'Sad'
    else:
        result = 'Mad'

    return result

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=2000)