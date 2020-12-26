import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IModel, defaultValue } from 'app/shared/model/model.model';

export const ACTION_TYPES = {
  FETCH_MODEL_LIST: 'model/FETCH_MODEL_LIST',
  FETCH_MODEL: 'model/FETCH_MODEL',
  CREATE_MODEL: 'model/CREATE_MODEL',
  UPDATE_MODEL: 'model/UPDATE_MODEL',
  DELETE_MODEL: 'model/DELETE_MODEL',
  RESET: 'model/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IModel>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ModelState = Readonly<typeof initialState>;

// Reducer

export default (state: ModelState = initialState, action): ModelState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MODEL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MODEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MODEL):
    case REQUEST(ACTION_TYPES.UPDATE_MODEL):
    case REQUEST(ACTION_TYPES.DELETE_MODEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_MODEL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MODEL):
    case FAILURE(ACTION_TYPES.CREATE_MODEL):
    case FAILURE(ACTION_TYPES.UPDATE_MODEL):
    case FAILURE(ACTION_TYPES.DELETE_MODEL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MODEL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MODEL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MODEL):
    case SUCCESS(ACTION_TYPES.UPDATE_MODEL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MODEL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/models';

// Actions

export const getEntities: ICrudGetAllAction<IModel> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MODEL_LIST,
  payload: axios.get<IModel>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IModel> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MODEL,
    payload: axios.get<IModel>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IModel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MODEL,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IModel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MODEL,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IModel> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MODEL,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
