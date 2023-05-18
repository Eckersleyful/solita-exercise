import axios from 'axios';
import React from 'react';
import { JOURNEY_URL} from '../constants/URLS';

class JourneyComponent extends React.Component {


    constructor(props) {

        super(props)

        this.state = {

            journeys: [],
            
            //Current page the user is on
            pageNumber: 1,

            //How many journeys we show on the table
            pageSize: 10, 
            
            //How many journeys there are in the DB
            journeyCount: 0,

            //Default sorting
            sortBy: "id",
            
            //Default ordering
            order: "asc",
            
            //Mapping all available sorting parameters
            sortParameters : {
                'Departure time': "departureDate",
                'Return time': "returnDate",
                'Departure station': "departureStation",
                'Return station': 'returnStation',
                'Distance': 'coveredDistance',
                'Duration': 'duration'
            },

            //Mapping ordering params
            orderParameters : {
                'Ascending': 'asc',
                'Descending': 'desc'
            },
            
            //Available page sizes
            pageSizeParameters : [5, 10, 15, 25, 50, 100]

        }
    }

    componentDidMount() {

        //Fetch initial data
        this.fetchJourneysByPage(this.state.pageNumber);
        this.fetchJourneyCount(this.state.pageSize); 
    }


    /*
    * Fetches how many journeys there are in the DB
    */

    fetchJourneyCount(){


        axios.get(JOURNEY_URL + "/count")
        .then((response) => {
            this.setState({
                journeyCount: response.data

            })
        })
        .catch(function (error) {
            console.log(error.toJSON());
        })
    }



    /**
     * Fetches journeys with sorting and ordering params.
     * 
     * @param {int} pageNumber   - The page number you want to fetch
     * @param {string} sortBy    - The param you want to sort on
     * @param {string} orderBy   - The param you want to order based on (asc/desc)
     * @param {int} pageSize     - How many records you want on the table
     */

    fetchJourneysByPage(pageNumber, sortBy = this.state.sortBy, order = this.state.order,
        pageSize = this.state.pageSize) {

        pageNumber -= 1

        axios.get(JOURNEY_URL + "?pageNumber=" + pageNumber
        + "&pageSize=" + pageSize + "&sortBy=" + sortBy + "&order=" + order)

        .then((response) => {
            this.setState({
                journeys: response.data
            })
        })

        .catch(function (error) {
            console.log(error);
        })
        
    }

    /**
     * Calculates how many pages there will be
     * with the given amount of journeys and page size
     * 
     * @param {int} journeyCount    - Amount of journeys, defaults to this.state.journeyCount
     * @param {int} recordsPerPage  - Page size, defautls to this.state.pageSize
     * @returns {int}               - Rounded up division of the params
     */
    getTotalPages(journeyCount = this.state.journeyCount, pageSize = this.state.pageSize){
        return Math.ceil(journeyCount / pageSize);
    }

    /**
     * Changes the pagination to the last page
     * 
     * @returns Returns early if we already are at last page
     */
    showLastPage = () => {
        
        const totalPages = this.getTotalPages();

        if(this.state.pageNumber >= totalPages){
            return;
        }

        this.setState({
            pageNumber: totalPages
        }) 

        this.fetchJourneysByPage(totalPages)

    }

    /**
     * Changes the pagination to the first page
     * @returns Returns early if we already are at first page
     */
    showFirstPage = () => {
        
        let firstPage = 1;

        if(this.state.pageNumber < firstPage){
            return
        }
            
        this.setState({
            pageNumber: firstPage
        })

        this.fetchJourneysByPage(firstPage)

    }


    /**
     * Changes the pagination to the next page
     * @returns Returns early if we already are at last page
     */
    showNextPage = () => {
        
        if(this.state.pageNumber >= this.getTotalPages()){
            console.log("hmm")
            return;
        }

        const newPage = this.state.pageNumber + 1;

        this.setState({
            pageNumber: newPage
        })

        this.fetchJourneysByPage(newPage);

    }

    /**
     * Changes pagination to the previous page
     * @returns Returns early if we already are at first page
     */
    showPreviousPage = () => {

        if(this.state.pageNumber <= 1){
            return;
        }

        const newPage = this.state.pageNumber - 1;

        this.setState({
            pageNumber: newPage
        })

        this.fetchJourneysByPage(newPage);

    }

    /**
     * The function fired by 
     * the Sort By <option> element with id sortby-select
     * @param {onClick event} event -Passed by onChange of the <option>
     */
    changeSortingParameter = (event) => {
        const parameter = event.target.value
        this.setState({
            sortBy: parameter
        })

    }

    /**
     * The function fired by 
     * the Order By <option> element with id orderby-select
     * @param {onClick event} event -Passed by onChange of the <option>
     */
    changeOrderingParameter = (event) => {
        
        const newOrder = event.target.value

        this.setState({
            order: newOrder
        })
    }

    /**
     * The function fired by 
     * the Page size <option> element with id page-size-select
     * @param {onClick event} event -Passed by onChange of the <option>
     */
    changePageSizeParameter = (event) => {
        
        const size = event.target.value

        this.setState({
            pageSize: size
        })
    }

    

    render() {
        

    
        const { journeys, pageNumber: currentPage, pageSize: recordsPerPage } = this.state;

        const pages = this.getTotalPages();


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
                                        <tr key= {journey.id}>
                                            <td key = {journey.id + "td-1"}>{(recordsPerPage * (currentPage - 1)) + index + 1}</td>
                                            <td key = {journey.id + "td-2"}>{journey.departureDate.split("T")[0]}<br></br>Klo {journey.departureDate.split("T")[1]}</td>
                                            <td key = {journey.id + "td-3"}>{journey.returnDate.split("T")[0]}<br></br>Klo {journey.returnDate.split("T")[1]}</td>
                                            <td key = {journey.id + "td-4"}>{journey.departureStation.stationName}</td>
                                            <td key = {journey.id + "td-5"}>{journey.returnStation.stationName}</td>
                                            <td key = {journey.id + "td-6"}>{(journey.coveredDistance / 1000).toFixed(2)} km</td>
                                            <td key = {journey.id + "td-7"}>{Math.ceil(journey.duration / 60)} mins</td>
                                        </tr>
                                    )
                                )
                            }
                        </tbody>
                    </table>
                </div>
                <div className = "center-div pagination-parent">
                        <div className = "page-number" style={{ float: 'left', fontFamily: 'monospace', color: '#0275d8' }}>
                            <p>Page {currentPage} of {pages}</p>
                        </div>
                        <div className = "navigation-buttons-parent">
                            <nav aria-label="Page navigation">
                                <ul className="pagination-list">
                                    <li className="page-item page-nav"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showPreviousPage}>Previous</a></li>
                                    <li className="page-item page-nav"><a type="button" className="page-link" disabled={currentPage === 1 ? true : false} onClick={this.showFirstPage}>First</a></li>
                                    <li className="page-item page-nav"><a type="button" className="page-link" disabled={currentPage === pages ? true : false} onClick={this.showNextPage}>Next</a></li>
                                    <li className="page-item page-nav"><a type="button" className="page-link" disabled={currentPage === pages ? true : false} onClick={this.showLastPage}>Last</a></li>
                                </ul>
                            </nav>
                        </div>

                </div>

                <div className = "sorting-parent-div center-div">

                    <div id = "sortby-select" className = "sorting-dropdown-div">
                        <select onChange={this.changeSortingParameter}>
                        <option>Sort By</option>
                        {Object.entries(sortMap).map(([key, value]) => (
                            <option value = {value} key = {key}>{key}</option>
                        ))}
                        </select>
                    </div>

                    <div id = "orderby-select" className = "sorting-dropdown-div">
                        <select onChange={this.changeOrderingParameter}>
                        <option>Order By</option>
                        {Object.entries(orderMap).map(([key, value]) => (
                            <option value = {value} key = {key}>{key}</option>
                        )) }
                        </select>
                    </div>
                    
                    <div id = "pagesize-select" className = "sorting-dropdown-div">
                        <select onChange={this.changePageSizeParameter}>
                        <option>Page Size</option>
                        {pageSizes.map((size) => (
                            <option value = {size} key = {size}>{size}</option>
                        ))}
                        </select>
                    </div>

                    <div className = "sorting-dropdown-div">
                        <button onClick={() => this.fetchJourneysByPage(1)}>
                            Sort
                        </button>
                    </div>
                </div>
            </div>
        )
    }
}

export default JourneyComponent;