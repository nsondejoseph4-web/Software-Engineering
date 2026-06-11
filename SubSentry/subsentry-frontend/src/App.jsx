import { useState, useEffect } from 'react'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { Doughnut } from 'react-chartjs-2'
import './App.css'

// Register Chart.js components globally
ChartJS.register(ArcElement, Tooltip, Legend);

function App() {
  // Data States
  const [metrics, setMetrics] = useState(null)
  const [subscriptions, setSubscriptions] = useState([])
  const [chartDataResults, setChartDataResults] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  // App Display Preferences
  const [baseCurrency, setBaseCurrency] = useState('USD')

  // Subscription Form Input States
  const [serviceName, setServiceName] = useState('')
  const [cost, setCost] = useState('')
  const [currency, setCurrency] = useState('USD') 
  const [category, setCategory] = useState('Entertainment')
  const [billingCycle, setBillingCycle] = useState('monthly')
  const [nextBillingDate, setNextBillingDate] = useState('')
  const [formMessage, setFormMessage] = useState('')

  const getCurrencySymbol = (code) => {
    if (code === 'GBP') return '£'
    if (code === 'EUR') return '€'
    return '$'
  }

  const fetchAllData = () => {
    const fetchMetrics = fetch(`http://localhost:8080/api/burn-rate/1/${baseCurrency}`).then(res => res.json())
    const fetchList = fetch('http://localhost:8080/api/subscriptions/1').then(res => res.json())
    const fetchChart = fetch(`http://localhost:8080/api/spend-by-category/1/${baseCurrency}`).then(res => res.json())

    Promise.all([fetchMetrics, fetchList, fetchChart])
      .then(([metricsData, listData, chartDataResultsData]) => {
        setMetrics(metricsData)
        setSubscriptions(listData)
        setChartDataResults(chartDataResultsData)
        setLoading(false)
      })
      .catch((err) => {
        setError(err.message)
        setLoading(false)
      })
  }

  useEffect(() => {
    fetchAllData()
  }, [baseCurrency])

  const handleCancelSubscription = (id) => {
    if (!window.confirm("Are you sure you want to stop tracking this subscription?")) return;
    fetch(`http://localhost:8080/api/delete-subscription/${id}`, { method: 'DELETE' })
      .then((res) => res.json())
      .then(() => {
        setFormMessage('🗑️ Subscription removed successfully!')
        fetchAllData()
      })
  }

  const handleAddSubscription = (e) => {
    e.preventDefault()
    const newSubPayload = {
      userId: 1,
      serviceName: serviceName,
      cost: parseFloat(cost),
      currency: currency, 
      category: category,
      billingCycle: billingCycle,
      nextBillingDate: nextBillingDate
    }

    fetch('http://localhost:8080/api/add-subscription', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newSubPayload)
    })
      .then((res) => res.json())
      .then((result) => {
        if (result.status === 'success') {
          setFormMessage('🎉 Subscription linked successfully!')
          setServiceName('')
          setCost('')
          setNextBillingDate('')
          fetchAllData()
        } else {
          setFormMessage('❌ Failed: ' + result.message)
        }
      })
      .catch((err) => setFormMessage('❌ Server error: ' + err.message))
  }

  // Construct Chart.js data matrix structure
  const mappedChartData = {
    labels: chartDataResults.map(item => item.name),
    datasets: [
      {
        label: 'Spending',
        data: chartDataResults.map(item => item.value),
        backgroundColor: [
          'rgba(136, 132, 216, 0.8)',
          'rgba(130, 202, 157, 0.8)',
          'rgba(255, 198, 88, 0.8)',
          'rgba(255, 128, 66, 0.8)',
          'rgba(0, 136, 254, 0.8)',
        ],
        borderColor: [
          '#8884d8',
          '#82ca9d',
          '#ffc658',
          '#ff8042',
          '#0088FE',
        ],
        borderWidth: 1,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: 'bottom' },
      tooltip: {
        callbacks: {
          label: function (context) {
            return ` ${context.label}: ${getCurrencySymbol(baseCurrency)}${context.parsed.toFixed(2)}`;
          }
        }
      }
    }
  };

  if (loading) return <div className="loading">Connecting to SubSentry Engine...</div>
  if (error) return <div className="error-msg">Error linking backend: {error}</div>
  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>🛡️ SubSentry</h1>
        <p>Active Financial Breakdown for User #{metrics?.userId}</p>
        <div className="global-currency-selector">
          <label>🌐 Set App Display Currency: </label>
          <select value={baseCurrency} onChange={(e) => setBaseCurrency(e.target.value)}>
            <option value="USD">USD ($)</option>
            <option value="GBP">GBP (£)</option>
            <option value="EUR">EUR (€)</option>
          </select>
        </div>
      </header>

      <main className="metrics-grid">
        <div className="metric-card bleed">
          <h3>Total Monthly Bleed</h3>
          <p className="amount">{getCurrencySymbol(baseCurrency)}{metrics?.totalMonthlyBleed?.toFixed(2)}</p>
          <span className="subtitle">Leaving your account every 30 days</span>
        </div>
        <div className="metric-card annual">
          <h3>Projected Annual Drain</h3>
          <p className="amount">{getCurrencySymbol(baseCurrency)}{metrics?.estimatedAnnualCost?.toFixed(2)}</p>
          <span className="subtitle">Total yearly commitment if un-optimized</span>
        </div>
      </main>

      {/* 📊 Visual Analytics Donut Chart Section */}
      <section className="chart-section" style={{ background: '#fff', padding: '20px', borderRadius: '12px', marginBottom: '20px', boxShadow: '0 4px 6px rgba(0,0,0,0.05)', color: '#333' }}>
        <h2>📊 Spending Analysis by Category</h2>
        <div style={{ width: '100%', height: 280, position: 'relative', margin: '0 auto' }}>
          {chartDataResults.length === 0 ? (
            <p style={{ textAlign: 'center', padding: '50px 0', color: '#999' }}>No visual analytics data found.</p>
          ) : (
            <Doughnut data={mappedChartData} options={chartOptions} />
          )}
        </div>
      </section>

      {/* 📋 Tracked Services List Table Grid */}
      <section className="table-section">
        <h2>📋 Tracked Services List</h2>
        <div className="table-wrapper">
          <table className="sub-table">
            <thead>
              <tr>
                <th>Service Name</th>
                <th>Cost</th>
                <th>Cycle</th>
                <th>Next Renewal Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {subscriptions.length === 0 ? (
                <tr>
                  <td colSpan="5" className="empty-row">No active subscriptions tracked.</td>
                </tr>
              ) : (
                subscriptions.map((sub) => (
                  <tr key={sub.id}>
                    <td className="service-name-cell">🔍 {sub.serviceName}</td>
                    <td className="cost-cell">
                      {getCurrencySymbol(baseCurrency)}{sub.cost.toFixed(2)} 
                      <span className="currency-badge"> {sub.currency}</span>
                    </td>
                    <td><span className={`cycle-tag ${sub.billingCycle}`}>{sub.billingCycle}</span></td>
                    <td>{sub.nextBillingDate}</td>
                    <td>
                      <button onClick={() => handleCancelSubscription(sub.id)} className="cancel-row-btn">Cancel</button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </section>

      {/* ➕ Tracking Input Form Section */}
      <section className="form-section">
        <h2>➕ Track a New Subscription</h2>
        <form onSubmit={handleAddSubscription} className="sub-form">
          <div className="input-group">
            <label>Service Name</label>
            <input 
              type="text" required value={serviceName} 
              onChange={(e) => setServiceName(e.target.value)} 
              placeholder="e.g. Disney+, Gym, Notion"
            />
          </div>

          <div className="input-group-row" style={{ display: 'flex', gap: '10px' }}>
            <div className="input-group" style={{ flex: 2 }}>
              <label>Cost</label>
              <input 
                type="number" step="0.01" required value={cost} 
                onChange={(e) => setCost(e.target.value)} 
                placeholder="0.00"
              />
            </div>
            <div className="input-group" style={{ flex: 1 }}>
              <label>Currency</label>
              <select value={currency} onChange={(e) => setCurrency(e.target.value)}>
                <option value="USD">USD ($)</option>
                <option value="GBP">GBP (£)</option>
                <option value="EUR">EUR (€)</option>
              </select>
            </div>
          </div>

          <div className="input-group">
            <label>Category Group</label>
            <select value={category} onChange={(e) => setCategory(e.target.value)}>
              <option value="Entertainment">Entertainment 🎬</option>
              <option value="Software">Software 💻</option>
              <option value="Gym">Gym/Health 🏋️</option>
              <option value="Food">Food/Groceries 🍎</option>
              <option value="Utilities">Utilities/Misc ⚡</option>
            </select>
          </div>

          <div className="input-group">
            <label>Billing Cycle</label>
            <select value={billingCycle} onChange={(e) => setBillingCycle(e.target.value)}>
              <option value="monthly">Monthly</option>
              <option value="yearly">Yearly</option>
            </select>
          </div>
          
          <div className="input-group">
            <label>Next Renewal Date</label>
            <input type="date" required value={nextBillingDate} onChange={(e) => setNextBillingDate(e.target.value)}/>
          </div>
          
          <button type="submit" className="submit-btn">Add to Tracker</button>
        </form>
        {formMessage && <p className="form-feedback">{formMessage}</p>}
      </section>
    </div>
  )
}

export default App
