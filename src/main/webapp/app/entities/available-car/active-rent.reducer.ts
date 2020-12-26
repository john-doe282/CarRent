import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IActiveRent, defaultValue } from 'app/shared/model/active-rent.model';

export const ACTION_TYPES = {
  FETCH_ACTIVERENT_LIST: 'activeRent/FETCH_ACTIVERENT_LIST',
  FETCH_ACTIVERENT: 'activeRent/FETCH_ACTIVERENT',
  CREATE_ACTIVERENT: 'activeRent/CREATE_ACTIVERENT',
  UPDATE_ACTIVERENT: 'activeRent/UPDATE_ACTIVERENT',
  DELETE_ACTIVERENT: 'activeRent/DELETE_ACTIVERENT',
  RESET: 'activeRent/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IActiveRent>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ActiveRentState = Readonly<typeof initialState>;

// Reducer

export default (state: ActiveRentState = initialState, action): ActiveRentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACTIVERENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACTIVERENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ACTIVERENT):
    case REQUEST(ACTION_TYPES.UPDATE_ACTIVERENT):
    case REQUEST(ACTION_TYPES.DELETE_ACTIVERENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ACTIVERENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACTIVERENT):
    case FAILURE(ACTION_TYPES.CREATE_ACTIVERENT):
    case FAILURE(ACTION_TYPES.UPDATE_ACTIVERENT):
    case FAILURE(ACTION_TYPES.DELETE_ACTIVERENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVERENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVERENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACTIVERENT):
    case SUCCESS(ACTION_TYPES.UPDATE_ACTIVERENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACTIVERENT):
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

const apiUrl = 'api/active-rents';

// Actions

export const getEntities: ICrudGetAllAction<IActiveRent> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVERENT_LIST,
    payload: axios.get<IActiveRent>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IActiveRent> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVERENT,
    payload: axios.get<IActiveRent>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IActiveRent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACTIVERENT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IActiveRent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACTIVERENT,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IActiveRent> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACTIVERENT,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
