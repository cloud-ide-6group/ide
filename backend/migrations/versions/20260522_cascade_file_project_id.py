"""add cascade delete for file.project_id

Revision ID: 20260522_cascade
Revises: f27a2da96acf
Create Date: 2026-05-22 00:00:00.000000
"""

from alembic import op

revision = "20260522_cascade"
down_revision = "f27a2da96acf"
branch_labels = None
depends_on = None


def upgrade():
    op.execute("""
        DO $$ 
        DECLARE 
            constraint_name text;
        BEGIN
            -- Находим constraint
            SELECT conname INTO constraint_name
            FROM pg_constraint 
            WHERE conrelid = 'file'::regclass 
            AND contype = 'f'
            AND conname LIKE '%project%';
            
            IF constraint_name IS NOT NULL THEN
                EXECUTE 'ALTER TABLE file DROP CONSTRAINT ' || constraint_name;
                EXECUTE 'ALTER TABLE file ADD CONSTRAINT ' || constraint_name || 
                        ' FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE';
            END IF;
        END $$;
    """)


def downgrade():
    op.execute("""
        DO $$ 
        DECLARE 
            constraint_name text;
        BEGIN
            SELECT conname INTO constraint_name
            FROM pg_constraint 
            WHERE conrelid = 'file'::regclass 
            AND contype = 'f'
            AND conname LIKE '%project%';
            
            IF constraint_name IS NOT NULL THEN
                EXECUTE 'ALTER TABLE file DROP CONSTRAINT ' || constraint_name;
                EXECUTE 'ALTER TABLE file ADD CONSTRAINT ' || constraint_name || 
                        ' FOREIGN KEY (project_id) REFERENCES project(id)';
            END IF;
        END $$;
    """)
