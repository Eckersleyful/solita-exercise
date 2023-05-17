import axios from 'axios';
import React from 'react';
import { STATION_URL }  from '../constants/URLS';

class StationComponent extends React.Component {


    constructor(props) {

        super(props)

        this.state = {

            stations: [],

            currentPage: 1,

            recordsPerPage: 30,
            
            stationCount: 0,

            sortBy: "id"

        }
    }

    async componentDidMount() {

        var [stationCount, stations] = await Promise.all([
            this.fetchStationCount(),
            this.fetchStationsByPage()
        ])

        this.setState({
            stations: stations.data,
            stationCount: stationCount.data

        })
    } 

    async fetchStationCount(){

        return axios.get(STATION_URL + "/count")
        .catch(function(error){
            console.log(error);
        })

    }

    async fetchStationDepartingCount(stationId){

        return axios.get(STATION_URL + "/departing/count?stationId=" +stationId)
        .catch(function(error){
            console.log(error);
        })

    }

    async fetchStationsByPage(pageNumber = this.state.currentPage) {

        pageNumber -= 1

        return axios.get(STATION_URL + "?pageNumber=" + pageNumber
            + "&pageSize=" + this.state.recordsPerPage)
            .catch(function(error) {
                console.log(error)
            })

    }


    getTotalPages(stationCount = this.state.stationCount, recordsPerPage = this.state.recordsPerPage){
        return Math.ceil(stationCount / recordsPerPage);
    }

    showLastPage = () => {
        
        const totalPages = this.getTotalPages();

        if(this.state.currentPage >= totalPages){
            return;
        }

        this.setState({
            currentPage: totalPages
        }) 

        this.fetchStationsByPage(totalPages);

    }

    showFirstPage = () => {
        
        let firstPage = 1;

        if(this.state.currentPage < firstPage){
            return
        }
            
        this.setState({
            currentPage: firstPage
        })

        this.fetchStationsByPage(firstPage)

    }

    showNextPage = () => {
        
        if(this.state.currentPage >= this.state.totalPages){
            return;
        }

        const newPage = this.state.currentPage + 1;

        this.setState({
            currentPage: newPage
        })

        this.fetchStationsByPage(newPage);

    }


    showPreviousPage = () => {
        


        if(this.state.currentPage <= 1){
            return;
        }

        const newPage = this.state.currentPage - 1;

        this.setState({
            currentPage: newPage
        })

        this.fetchStationsByPage(newPage);

    }


    render() {
        const { stations, currentPage, recordsPerPage} = this.state;

        const totalPages = this.getTotalPages();
        return (
            <div className = "main-div">

                <div className = "center-div">
                    <h1>Helsinki Citybike journeys</h1>
                </div>
                
                <div className = "data-table-wrapper center-div">
                    <table className = "data-table">
                        <thead id = "table-header">
                            <tr>
                                <th>Index</th>
                                <th>Station name</th>
                                <th>Departure count</th>
                                <th>Return count</th>
                            </tr>
                        </thead>
                        <tbody>
                            {stations.length === 0 ?
                                <tr>
                                    <td>No stations found</td>
                                </tr> :
                                stations.map(
                                    (station, index) => (
                                        <tr key = {station.id + "meow"}>
                                            <td>{(recordsPerPage * (currentPage - 1)) + index + 1}</td>
                                            <td>{station.stationName}</td>
                                            <td>{station.departingJourneysCount}</td>
                                            <td>{station.returningJourneysCount}</td>
                                        </tr>
                                    )
                                )
                            }
                        </tbody>
                    </table>
                </div>
                <div className = "center-div pagination-parent">
                    <div style={{ float: 'left', fontFamily: 'monospace', color: '#0275d8' }}>
                        <p>Page {currentPage} of {totalPages}</p> 
                    </div>
                    <div id = "station-navigation-parent" className = "navigation-buttons-parent">
                        <nav aria-label="Page navigation">
                            <ul className="pagination-list">
                                <li className="page-item"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showPreviousPage}>Previous</a></li>
                                <li className="page-item"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showFirstPage}>First</a></li>
                                <li className="page-item"><a type="button" className="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showNextPage}>Next</a></li>
                                <li className="page-item"><a type="button" className="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showLastPage}>Last</a></li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        )
    }
}

export default StationComponent;