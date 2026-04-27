from werkzeug.security import generate_password_hash, check_password_hash
import pytest
from app.shared.features.password_hash.service import (
    check_password_with_hash,
    get_password_hash,
)


@pytest.mark.parametrize(
    "password",
    [
        "simple",
        "complex!@#$%",
        "very_long_password_" * 10,
        "12345",
        " with spaces ",
        "русский_пароль",
        "admin",
        "password123",
    ],
)
def test_password_hashing(password):
    """Тест хеширования пароля"""

    hashed = get_password_hash(password)

    assert check_password_hash(hashed, password) is True


def test_password_checking():
    """Тест проверки пароля"""

    password = "test"
    hashed = get_password_hash(password)
    correct = check_password_with_hash(hashed, password)
    assert correct == True
    wrong = check_password_with_hash(hashed, "not test")
    assert wrong == False
