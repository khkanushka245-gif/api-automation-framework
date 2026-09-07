import json
import os

import pytest

DATA_PATH = os.path.join(os.path.dirname(__file__), "..", "data", "users.json")


def _load_user_ids():
    with open(DATA_PATH) as f:
        return [u["id"] for u in json.load(f)["users"]]


def test_get_user_returns_expected_fields(api_client):
    resp = api_client.get("/users/1")
    assert resp.status_code == 200
    body = resp.json()
    assert {"id", "name", "email"}.issubset(body.keys())


@pytest.mark.parametrize("user_id", _load_user_ids())
def test_users_are_reachable(api_client, user_id):
    resp = api_client.get(f"/users/{user_id}")
    assert resp.status_code == 200
    assert resp.json()["id"] == user_id


def test_create_post(api_client):
    payload = {"title": "demo", "body": "sample body", "userId": 1}
    resp = api_client.post("/posts", json=payload)
    assert resp.status_code == 201
    assert resp.json()["title"] == "demo"
