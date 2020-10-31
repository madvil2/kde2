import shutil

import pandas as pd
from docxtpl import DocxTemplate
from datetime import date

from src.utils import get_connection


_MONITORING_CARD_PATH = 'templates/pet_card.docx'
_SHELTER_ANIMALS_PATH = 'templates/shelter_animals.docx'
_AGGREGATED_SHELTERS = 'templates/aggregated_shelters.docx'
test_img_path = 'templates/dogo.png'
shutil.copy('123.png', test_img_path)


def _fill_empty_tables(tables_list, args_dict):
    for table in tables_list:
        if table not in args_dict:
            args_dict[table] = [{}]
    return args_dict


def _add_current_date(args_dict):
    _MONTH_LIST = ['января', 'февраля', 'марта', 'апреля',
                   'мая', 'июня', 'июля', 'августа',
                   'сентября', 'октября', 'ноября', 'декабря']
    args_dict['cur_date'] = date.today().day
    args_dict['cur_month'] = _MONTH_LIST[date.today().month - 1]
    args_dict['cur_year'] = date.today().year
    return args_dict


def fill_monitoring_card(animal_id, output_path):
    conn = get_connection()
    query = f"""select card_number pet_id, age pet_age, weight pet_weight, name pet_name,
    s.value pet_sex, sp.value pet_type, col.value pet_color, e.value pet_ears,
    t.value pet_tail, si.value pet_size, f.value pet_fur
    FROM animal a
    INNER JOIN dict_species sp ON sp.id=a.species_id
    INNER JOIN dict_color col ON col.id=a.color_id
    INNER JOIN dict_sex s ON s.id=a.sex_id
    INNER JOIN dict_fur f ON f.id=a.fur_id
    INNER JOIN dict_ear e ON e.id=a.ear_id
    INNER JOIN dict_tail t ON t.id=a.tail_id
    INNER JOIN dict_size si ON si.id=a.size_id
    INNER JOIN file ON file.id=a.file_id
    WHERE a.id = {animal_id}
    """
    data_df = pd.read_sql_query(query, con=conn)
    empty_row = ['__' for _ in range(len(data_df.columns))]
    data_df.loc[data_df.shape[0]] = empty_row
    # card_args = ['', '', '', '', 'adress', 'org_name', 'valier_id', '',
    #              '', '', '', '', 'pet_breed', '',
    #              '', '', '', '', 'pet_special_sign', 'pet_temper',
    #              'mark_id',
    #              'steril_date', 'steril_month', 'steril_year', 'steril_place', 'steril_doc', 'steril_flag',
    #              'order_id', 'steril_date', 'steril_month', 'steril_year',
    #              'trap_id', 'trap_date', 'trap_month', 'trap_year', 'trap_location', 'trap_video']
    card_tables = ['tbl_treat_parasites', 'tbl_vaccine', 'tbl_health']

    render_dict = data_df.loc[0].to_dict()
    render_dict = _add_current_date(render_dict)
    render_dict = _fill_empty_tables(card_tables, render_dict)

    render_dict['tbl_health'] = [{'id': 1}, {'id': 2}]

    tpl = DocxTemplate(_MONITORING_CARD_PATH)
    tpl.replace_pic('pet_photo.png', test_img_path)
    tpl.render(render_dict)
    tpl.save(output_path)
    conn.close()


def fill_shelter_animals(shelter_id, output_path):
    conn = get_connection()
    query = f"""select card_number pet_id, name pet_name,
    s.value pet_sex, sp.value pet_type
    FROM animal a
    INNER JOIN dict_species sp ON sp.id=a.species_id
    INNER JOIN dict_sex s ON s.id=a.sex_id
    WHERE a.shelter_id = {shelter_id}
    """
    data_df = pd.read_sql_query(query, con=conn)
    data_df = data_df.reset_index()
    render_dict = {'tbl_animals': data_df.to_dict('records')}
    render_dict = _add_current_date(render_dict)

    tpl = DocxTemplate(_SHELTER_ANIMALS_PATH)
    tpl.render(render_dict)
    tpl.save(output_path)
    conn.close()


def fill_aggregated_shelters(prefect_id, date_from, date_to, output_path):
    conn = get_connection()
    query = f"""select card_number pet_id, sp.value pet_type, date, shelter_name, status
    FROM animal_clickstream a
    INNER JOIN dict_species sp ON sp.id=a.species_id
    WHERE a.date <= {date_to}
        AND a.prefect_id = {prefect_id}
    """
    data_df = pd.read_sql_query(query, con=conn)
    end_df = data_df.groupby('shelter_name').count()

    start_df = data_df[data_df['date'] < date_from].groupby('shelter_name').count()

    arr_df = data_df[(data_df['date'] > date_from)
                     & (data_df['status'] == 'arrive')].groupby('shelter_name').count()

    dep_df = data_df[(data_df['date'] > date_from)
                     & (data_df['status'] == 'depart')].groupby('shelter_name').count()

    agg_df = end_df.merge(start_df, how='left', on='shelter_name')\
        .merge(arr_df, how='left', on='shelter_name')\
        .merge(dep_df, how='left', on='shelter_name')

    counts = agg_df.sum()
    agg_df.loc[agg_df.shape[0]] = ['Итого:'] + counts
    render_dict = {'tbl_aggregated': data_df.to_dict('records')}

    tpl = DocxTemplate(_AGGREGATED_SHELTERS)
    tpl.render(render_dict)
    tpl.save(output_path)
    conn.close()


# card_args = ['pet_id', 'cur_date', 'cur_month', 'cur_year', 'adress', 'org_name', 'valier_id', 'pet_type',
#              'pet_age', 'pet_weight', 'pet_name', 'pet_sex', 'pet_race', 'pet_color',
#              'pet_fur', 'pet_ears', 'pet_tail', 'pet_size', 'pet_special_sign', 'pet_temper',
#              'mark_id',
#              'steril_date', 'steril_month', 'steril_year', 'steril_place', 'steril_doc', 'steril_flag',
#              'order_id', 'steril_date', 'steril_month', 'steril_year',
#              'trap_id', 'trap_date', 'trap_month', 'trap_year', 'trap_location', 'trap_video']
