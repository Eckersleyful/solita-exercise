import axios from 'axios';
import React from 'react';

class JourneyComponent extends React.Component {


    constructor(props) {

        super(props)

        this.state = {

            stations: [],

            currentPage: 1,

            recordsPerPage: 30,
            
            totalPages: 0,

            sortBy: "id"

        }
    }

    componentDidMount() {
        this.fetchStationsByPage(this.state.currentPage);
        this.fetchTotalPages(this.state.recordsPerPage); 
    } 

    fetchTotalPages(recordsPerPage){
        axios.get("http://localhost:8080/stations/count")
        .then(response => response.data).then((data) => {
            this.setState({
                totalPages: Math.ceil(data / recordsPerPage)

            })
        })
        .catch(function(error) {
            console.log(error)
        })
    }

    fetchStationsByPage(pageNumber) {


        pageNumber -= 1

        axios.get("http://localhost:8080/stations?pageNumber=" + pageNumber
            + "&pageSize=" + this.state.recordsPerPage)
            .then(response => response.data).then((data) => {
                this.setState({
                    stations: data

                })

            })
            .catch(function(error) {
                console.log(error)
            })

    }
    showLastPage = () => {
        
        if(this.state.currentPage >= this.state.totalPages){
            return;
        }

        this.setState({
            currentPage: this.state.totalPages
        }) 

        this.fetchStationsByPage(this.state.totalPages)

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
        const { stations, currentPage, recordsPerPage, totalPages } = this.state;

        return (
            <div>

                <h1 className="text-center mt-5 ">All bike journeys</h1>
                <div className="container mt-2">
                    <table className="table table-bordered border-info shadow">
                        <thead>
                            <tr>
                                <th>Index</th>
                                <th>Station name</th>
                            </tr>
                        </thead>
                        <tbody>
                            {stations.length === 0 ?
                                <tr align="center"><td colSpan="5">No stations found</td></tr> :
                                stations.map(
                                    (station, index) => (

                                        <tr key={station.id}>
                                            <td>{(recordsPerPage * (currentPage - 1)) + index + 1}</td>
                                            <td>{station.stationName}</td>
                                        </tr>
                                    )
                                )
                            }
                        </tbody>
                    </table>
                    <table className="table">
                        <div style={{ float: 'left', fontFamily: 'monospace', color: '#0275d8' }}>
                            <p>Page {currentPage} of {totalPages}</p> 
                        </div>
                        <div style={{ float: 'right' }}>
                            <div className="clearfix"></div>
                            <nav aria-label="Page navigation example">
                                <ul className="pagination">
                                    <li className="page-item"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showPreviousPage}>Previous</a></li>
                                    <li className="page-item"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showFirstPage}>First</a></li>
                                    <li className="page-item"><a type="button" className="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showNextPage}>Next</a></li>
                                    <li className="page-item"><a type="button" className="page-link" disabled={currentPage === totalPages ? true : false} onClick={this.showLastPage}>Last</a></li>
                                </ul>
                            </nav>
                        </div>
                        



                    </table>
                </div>
            </div>
        )
    }
}

export default JourneyComponent;