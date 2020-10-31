from psycopg2 import connect
from configparser import ConfigParser


def get_connection():
    """Create postgres connection"""

    config_path = 'config.ini'

    config = ConfigParser()
    config.read(config_path)

    conn = connect(
        host=config.get('db', 'DB_HOST'),
        port=config.get('db', 'DB_PORT'),
        dbname=config.get('db', 'DB_NAME'),
        user=config.get('db', 'DB_USER'),
        password=config.get('db', 'DB_PASS')
    )
    return conn
