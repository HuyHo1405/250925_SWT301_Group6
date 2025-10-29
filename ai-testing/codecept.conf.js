const { setHeadlessWhen, setCommonPlugins } = require('@codeceptjs/configure');

require('dotenv').config();

// turn on headless mode when running with HEADLESS=true environment variable
// export HEADLESS=true && npx codeceptjs run
setHeadlessWhen(process.env.HEADLESS);

// enable all common plugins https://github.com/codeceptjs/configure#setcommonplugins
setCommonPlugins();



/** @type {CodeceptJS.MainConfig} */
exports.config = {
  tests: './tests/*_test.js',
  output: 'output',
  helpers: {
    Playwright: {
      browser: 'chromium',
      url: 'http://localhost',
      show: true
    }
  },
  include: {
    I: './steps_file.js'
  },
  plugins: {
    htmlReporter: {
      enabled: true
    },

  },

    ai: {
        request: async messages => {
            const Groq = require('groq-sdk')

            const client = new Groq({
                apiKey: process.env.GROQ_API_KEY, // This is the default and can be omitted
            })

            const chatCompletion = await groq.chat.completions.create({
                messages,
                model: 'llama-3.3-70b-versatile',
            })
            return chatCompletion.choices[0]?.message?.content || ''
        }
    },
    name: 'ai-testing'
}

