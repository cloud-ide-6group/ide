from .repository import language_repo


def get_all_langs():
    """
    Получает массив языков только с нужными полями.

    Returns:
        list: Массив словарей
    """
    langs = language_repo.get_all_langs()

    result = []
    for l in langs:
        result.append({"id": l.id, "name": l.name, "description": l.description})

    return result


def lang_extsts(id):
    """
    Проверяет наличие языкав базе данных

    Returns:
        bool: True, если язык есть, иначе False
    """
    language = language_repo.get_lang(id)

    if language == None:
        return False

    return True
