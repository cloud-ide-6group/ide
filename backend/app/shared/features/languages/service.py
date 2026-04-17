from .repository import language_repo


def get_all_langs():
    langs = language_repo.get_all_langs()

    result = []
    for l in langs:
        result.append({"id": l.id, "name": l.name, "description": l.description})

    return result
