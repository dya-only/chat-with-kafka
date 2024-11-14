import express, { json } from 'express'
import { Kafka } from 'kafkajs'

const app = express()
app.use(json()) // JSON 파싱을 위한 미들웨어

// Kafka 클라이언트 설정
const kafka = new Kafka({
        clientId: 'my-app',
        brokers: [
                'b-2.kafkacluster.u263sj.c2.kafka.ap-northeast-2.amazonaws.com:9096',
                'b-1.kafkacluster.u263sj.c2.kafka.ap-northeast-2.amazonaws.com:9096'
        ],
        ssl: true,
        sasl: {
                mechanism: 'scram-sha-512',
                username: 'dya-only',
                password: 'password'
        }
})

// 프로듀서와 컨슈머 생성
const producer = kafka.producer()
const consumer = kafka.consumer({ groupId: 'test' })

async function createTopic(topicName) {
        const admin = kafka.admin()
        await admin.connect()
        const topics = await admin.listTopics()
        if (!topics.includes(topicName)) {
                await admin.createTopics({
                        topics: [{ topic: topicName }]
                })
                console.log(`토픽 ${topicName} 생성 완료`)
        } else {
                console.log(`토픽 ${topicName} 이미 존재함`)
        }
        await admin.disconnect()
}

async function run() {
        // 토픽 생성 (존재하지 않을 경우)
        await createTopic('test')

        // 프로듀서 연결
        await producer.connect()
        console.log('producer 연결 완료')

        // 컨슈머 연결
        await consumer.connect()
        console.log('consumer 연결 완료')

        // 토픽 구독
        await consumer.subscribe({ topic: 'test', fromBeginning: true })

        // 메시지 수신 처리
        consumer.run({
                eachMessage: async ({ topic, partition, message }) => {
                        console.log(
                                `수신된 메시지: ${message.value.toString()}`
                        )
                }
        })
}

// Kafka 클라이언트 실행
run().catch(console.error)

// 메시지 프로듀싱 엔드포인트
app.post('/produce', async (req, res) => {
        const { message } = req.body

        try {
                await producer.send({
                        topic: 'test',
                        messages: [{ value: message }]
                })
                res.status(200).send('메시지가 Kafka로 전송되었습니다')
        } catch (err) {
                console.error('Kafka로 메시지 전송 중 오류 발생:', err)
                res.status(500).send('Kafka로 메시지 전송 중 오류 발생')
        }
})

// 서버 시작
app.listen(3000, () => {
        console.log('Express 서버가 포트 3000에서 실행 중입니다')
})
