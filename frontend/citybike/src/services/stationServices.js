import axios from 'axios';
import { STATION_URL }  from '../constants/URLS';

async function fetchStationCount(){

    return axios.get(STATION_URL + "/count")
    .catch(function(error){
        console.log(error);
    })

}

async function fetchStationsByPage(pageNumber, pageSize) {

    pageNumber -= 1

    return axios.get(STATION_URL + "?pageNumber=" + pageNumber
        + "&pageSize=" + pageSize)
        .catch(function(error) {
            console.log(error)
        })

}

export {fetchStationCount, fetchStationsByPage}