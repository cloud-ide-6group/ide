from app.shared.base_repositories import BaseFileRepository, BaseProjectRepository


class FileRepository(BaseFileRepository):
    pass


class ProjectRepository(BaseProjectRepository):
    pass


file_repo = FileRepository()
project_repo = ProjectRepository()
