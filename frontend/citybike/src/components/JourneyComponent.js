import axios from 'axios';
import React from 'react';

class JourneyComponent extends React.Component {


    constructor(props) {

        super(props)

        this.state = {

            journeys: [],

            currentPage: 1,

            recordsPerPage: 10, 
            
            totalPages: 0,

            sortBy: "id",

            order: "asc",

            sortParameters : {
                'Departure time': "departureDate",
                'Return time': "returnDate",
                'Departure station': "departureStation",
                'Return station': 'returnStation',
                'Distance': 'coveredDistance',
                'Duration': 'duration'
            },

            orderParameters : {
                'Ascending': 'asc',
                'Descending': 'desc'
            },

            pageSizeParameters : [5, 10, 15, 25, 50, 100]

        }
    }

    componentDidMount() {
        this.fetchJourneysByPage(this.state.currentPage);
        this.fetchTotalPages(this.state.recordsPerPage); 
    }


    fetchTotalPages(){


        axios.get("http://localhost:8080/journeys/count")
        .then((response) => {
            this.setState({
                totalPages: response.data

            })
        })
        .catch(function (error) {
            console.log(error.toJSON());
        })



    }

    fetchJourneysByPage(pageNumber, searchParameter = this.state.sortBy, order = this.state.order,
        pageSize = this.state.recordsPerPage) {

        pageNumber -= 1

        console.log(searchParameter)
        console.log(order)

        axios.get("http://localhost:8080/journeys?pageNumber=" + pageNumber
        + "&pageSize=" + pageSize + "&sortBy=" + searchParameter + "&order=" + order)
        .then((response) => {

            for(let x in response.data){
                response.data[x].coveredDistance = Math.ceil(response.data[x].coveredDistance / 1000);
                response.data[x].duration = Math.ceil(response.data[x].duration / 60); 
            }

            this.setState({
                journeys: response.data
            })
        })
        .catch(function (error) {
            console.log(error);
        })
        
    }

    showLastPage = () => {
        
        if(this.state.currentPage >= this.state.totalPages){
            return;
        }

        this.setState({
            currentPage: this.state.totalPages
        }) 

        this.fetchJourneysByPage(this.state.totalPages)

    }

    showFirstPage = () => {
        
        let firstPage = 1;

        if(this.state.currentPage < firstPage){
            return
        }
            
        this.setState({
            currentPage: firstPage
        })

        this.fetchJourneysByPage(firstPage)

    }

    showNextPage = () => {
        
        if(this.state.currentPage >= this.state.totalPages){
            return;
        }

        const newPage = this.state.currentPage + 1;

        this.setState({
            currentPage: newPage
        })

        this.fetchJourneysByPage(newPage);

    }



    showPreviousPage = () => {

        if(this.state.currentPage <= 1){
            return;
        }

        const newPage = this.state.currentPage - 1;

        this.setState({
            currentPage: newPage
        })

        this.fetchJourneysByPage(newPage);

    }

    changeSortingParameter = (event) => {
        const parameter = event.target.value
        this.setState({
            sortBy: parameter
        })

    }

    changeOrderingParameter = (event) => {
        
        const newOrder = event.target.value

        this.setState({
            order: newOrder
        })
    }

    changePageSizeParameter = (event) => {
        
        const size = event.target.value

        this.setState({
            recordsPerPage: size
        })
    }

    

    render() {
        
        const { journeys, currentPage, recordsPerPage } = this.state;

        const pages = Math.ceil(this.state.totalPages / this.state.recordsPerPage);


        const sortMap = this.state.sortParameters;
        const orderMap = this.state.orderParameters;
        const pageSizes = this.state.pageSizeParameters;
        
        return (
            <div className = "main-div">
                <div className = "center-div">
                    <h1>Helsinki Citybike journeys</h1>
                </div>
                
                <div className = "data-table-wrapper center-div">
                    <table className = "data-table">
                        <thead id = "table-header">
                            <tr>
                                <th>number</th>
                                <th>Departure time</th>
                                <th>Return time</th>
                                <th>Departure station</th>
                                <th>Return station</th>
                                <th>Journey distance</th>
                                <th>Journey duration</th>
                            </tr>
                        </thead>
                        <tbody>
                            {journeys.length === 0 ?
                                <tr><td>No journeys found</td></tr> :
                                journeys.map(
                                    (journey, index) => (
                                        <tr key={journey.id}>
                                            <td>{(recordsPerPage * (currentPage - 1)) + index + 1}</td>
                                            <td>{journey.departureDate.split("T")[0]}<br></br>Klo {journey.departureDate.split("T")[1]}</td>
                                            <td>{journey.returnDate.split("T")[0]}<br></br>Klo {journey.returnDate.split("T")[1]}</td>
                                            <td>{journey.departureStation.stationName}</td>
                                            <td>{journey.returnStation.stationName}</td>
                                            <td>{journey.coveredDistance} km</td>
                                            <td>{journey.duration} m</td>

                                        </tr>
                                    )
                                )
                            }
                        </tbody>
                    </table>
                </div>
                <div className = "center-div">
                    <table>
                        <div style={{ float: 'left', fontFamily: 'monospace', color: '#0275d8' }}>
                            Page {currentPage} of {pages}
                        </div>
                        <div>
                            <div className="clearfix"></div>
                            <nav aria-label="Page navigation example">
                                <ul className="pagination">
                                    <li className="page-item page-nav"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showPreviousPage}>Previous</a></li>
                                    <li className="page-item page-nav"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showFirstPage}>First</a></li>
                                    <li className="page-item page-nav"><a type="button" className="page-link" disabled={currentPage === pages ? true : false} onClick={this.showNextPage}>Next</a></li>
                                    <li className="page-item page-nav"><a type="button" className="page-link" disabled={currentPage === pages ? true : false} onClick={this.showLastPage}>Last</a></li>
                                </ul>
                            </nav>
                        </div>
                    
                    </table>
                </div>
                <div className = "sorting-parent-div center-div">
                    <div className = "sorting-main-div-child">
                        <select onChange={this.changeSortingParameter}>
                        <option>Sort By</option>
                        {Object.entries(sortMap).map(([key, value]) => (
                            <option value = {value} key = {key}>{key}</option>
                        ))}
                        </select>
                    </div>

                    <div>
                        <select onChange={this.changeOrderingParameter}>
                        <option>Order By</option>
                        {Object.entries(orderMap).map(([key, value]) => (
                            <option value = {value} key = {key}>{key}</option>
                        ))}
                        </select>
                    </div>
                    
                    <div>
                        <select onChange={this.changePageSizeParameter}>
                        <option>Page Size</option>
                        {pageSizes.map((size) => (
                            <option value = {size} key = {size}>{size}</option>
                        ))}
                        </select>
                    </div>
                    <button onClick={() => this.fetchJourneysByPage(1)}>
                        Sort
                    </button>
                </div>
            </div>
        )
    }
}

export default JourneyComponent;