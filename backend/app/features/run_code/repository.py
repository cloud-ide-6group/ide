from app.shared.base_repositories import BaseProjectRepository, BaseLanguageRepository


class ProjectRepository(BaseProjectRepository):
    pass


class LanguageRepository(BaseLanguageRepository):
    pass


project_repo = ProjectRepository()
language_repo = LanguageRepository()
